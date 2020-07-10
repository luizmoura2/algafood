package com.algaworks.algafood.infrastructure.repository;

import java.lang.reflect.Field;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.ReflectionUtils;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
	implements CustomJpaRepository<T, ID> {

	private EntityManager manager;
	
	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, 
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		
		this.manager = entityManager;
	}

	@Override
	public Optional<T> buscarPrimeiro() {
		var jpql = "from " + getDomainClass().getName();
		
		T entity = manager.createQuery(jpql, getDomainClass())
			.setMaxResults(1)
			.getSingleResult();
		
		return Optional.ofNullable(entity);
	}

	public T mergeParc(T source, final T target) {
		
	    ReflectionUtils.doWithFields(target.getClass(), new ReflectionUtils.FieldCallback() {
		    @Override
		    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
		      field.setAccessible(true);
		      if (field.get(source) != null){		    	
		    	  field.set(target, field.get(source));
		      }
		    }
	  }, ReflectionUtils.COPYABLE_FIELDS);
	 
	    return target;
	}
	
	@Override
	public void detach(T entity) {
		manager.detach(entity);
	}
}