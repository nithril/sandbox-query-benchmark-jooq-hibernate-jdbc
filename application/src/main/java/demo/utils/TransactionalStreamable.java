package demo.utils;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by nlabrot on 05/10/15.
 */
public class TransactionalStreamable<T> {

	private final PlatformTransactionManager platformTransactionManager;

	private final Callable<Stream<T>> callable;

	public TransactionalStreamable(PlatformTransactionManager platformTransactionManager, Callable<Stream<T>> callable) {
		this.platformTransactionManager = platformTransactionManager;
		this.callable = callable;
	}

	public Stream stream() {
		TransactionTemplate txTemplate = new TransactionTemplate(platformTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.setReadOnly(true);

		TransactionStatus transaction = platformTransactionManager.getTransaction(txTemplate);

		try {
			return callable.call().onClose(() -> {
				platformTransactionManager.commit(transaction);
			});
		} catch (Exception e) {
			platformTransactionManager.rollback(transaction);
			throw new RuntimeException(e);
		}
	}

	public void forEach(Consumer<T> c) {
		try (Stream<T> s = stream()){
			s.forEach(c);
		}
	}
}
