package persistence.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import persistence.entities.AEntity;

public abstract class ADAO {

	private static EntityManagerFactory entityManagerFactory;

	private static EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence
					.createEntityManagerFactory("test_inmem_db");
		}
		return entityManagerFactory;
	}

	protected EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}

	protected <T extends AEntity> T create(final T entity) {
		final EntityManager em = getEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		em.persist(entity);
		et.commit();
		em.close();
		return entity;
	}

	protected <T extends AEntity> T read(final Class<? extends T> clazz,
			final long id) {
		final EntityManager em = getEntityManager();
		final EntityTransaction et = em.getTransaction();

		et.begin();
		final T result = em.find(clazz, id);
		et.commit();
		em.close();

		return result;
	}

	protected final <T extends AEntity> List<T> readByJPQL(
			final String jpqString) {
		final EntityManager em = getEntityManager();
		final EntityTransaction et = em.getTransaction();

		et.begin();
		final Query q = em.createQuery(jpqString);
		@SuppressWarnings("unchecked")
		final List<T> list = q.getResultList();
		et.commit();
		em.close();
		return list;
	}

	protected <T extends AEntity> T update(final T entity) {
		final EntityManager em = getEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		em.merge(entity);
		et.commit();
		em.close();
		return entity;
	}

	protected void delete(final AEntity entity) {
		final EntityManager em = getEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		em.remove(em.find(entity.getClass(), entity.getId()));
		et.commit();
		em.close();
	}
}
