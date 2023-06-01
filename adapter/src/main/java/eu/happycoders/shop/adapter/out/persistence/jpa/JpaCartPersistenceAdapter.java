package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.application.port.out.persistence.CartPersistencePort;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.customer.CustomerId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;

public class JpaCartPersistenceAdapter implements CartPersistencePort {

  private final EntityManagerFactory entityManagerFactory;

  public JpaCartPersistenceAdapter(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void save(Cart cart) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      entityManager.getTransaction().begin();

      CartJpaEntity cartJpaEntity = CartMapper.toJpaEntity(cart);
      entityManager.merge(cartJpaEntity);

      entityManager.getTransaction().commit();
    }
  }

  @Override
  public Optional<Cart> findByCustomerId(CustomerId customerId) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      CartJpaEntity cartJpaEntity = entityManager.find(CartJpaEntity.class, customerId.value());
      return CartMapper.toCartModelEntity(cartJpaEntity);
    }
  }

  @Override
  public void deleteById(CustomerId customerId) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      entityManager.getTransaction().begin();

      CartJpaEntity cartJpaEntity = entityManager.find(CartJpaEntity.class, customerId.value());

      if (cartJpaEntity != null) {
        entityManager.remove(cartJpaEntity);
      }

      entityManager.getTransaction().commit();
    }
  }
}
