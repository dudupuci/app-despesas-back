package io.github.dudupuci.appdespesas.config.persistence;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransacionalInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TransacionalInterceptor.class);

    private final PlatformTransactionManager txManager;

    public TransacionalInterceptor(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    @Around("@annotation(io.github.dudupuci.appdespesas.config.persistence.Transacional) || @within(io.github.dudupuci.appdespesas.config.persistence.Transacional)")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        boolean existingTx = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionStatus status = null;
        boolean created = false;
        Object result;

        try {
            if (!existingTx) {
                log.debug("TransacionalInterceptor: no existing transaction, creating one");
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                status = txManager.getTransaction(def);
                created = true;
            } else {
                log.debug("TransacionalInterceptor: existing transaction detected, joining");
            }

            result = pjp.proceed();

            if (created) {
                log.debug("TransacionalInterceptor: committing transaction");
                txManager.commit(status);
            }

            return result;
        } catch (Throwable t) {
            if (created && !status.isCompleted()) {
                try {
                    log.debug("TransacionalInterceptor: rollback due to exception");
                    txManager.rollback(status);
                } catch (Exception ex) {
                    log.warn("TransacionalInterceptor: error during rollback", ex);
                }
            }
            throw t;
        }
    }
}
