package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Item;
import entidades.ItemVenda;
import entidades.Venda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class ItemVendaJpaController implements Serializable {

    public ItemVendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemVenda itemVenda) {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Item idItem = itemVenda.getIdItem();
            if (idItem != null)
            {
                idItem = em.getReference(idItem.getClass(), idItem.getId());
                itemVenda.setIdItem(idItem);
            }
            Venda idVenda = itemVenda.getIdVenda();
            if (idVenda != null)
            {
                idVenda = em.getReference(idVenda.getClass(), idVenda.getId());
                itemVenda.setIdVenda(idVenda);
            }
            em.persist(itemVenda);
            if (idItem != null)
            {
                idItem.getItemVendaList().add(itemVenda);
                idItem = em.merge(idItem);
            }
            if (idVenda != null)
            {
                idVenda.getItemVendaList().add(itemVenda);
                idVenda = em.merge(idVenda);
            }
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void edit(ItemVenda itemVenda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemVenda persistentItemVenda = em.find(ItemVenda.class, itemVenda.getId());
            Item idItemOld = persistentItemVenda.getIdItem();
            Item idItemNew = itemVenda.getIdItem();
            Venda idVendaOld = persistentItemVenda.getIdVenda();
            Venda idVendaNew = itemVenda.getIdVenda();
            if (idItemNew != null)
            {
                idItemNew = em.getReference(idItemNew.getClass(), idItemNew.getId());
                itemVenda.setIdItem(idItemNew);
            }
            if (idVendaNew != null)
            {
                idVendaNew = em.getReference(idVendaNew.getClass(), idVendaNew.getId());
                itemVenda.setIdVenda(idVendaNew);
            }
            itemVenda = em.merge(itemVenda);
            if (idItemOld != null && !idItemOld.equals(idItemNew))
            {
                idItemOld.getItemVendaList().remove(itemVenda);
                idItemOld = em.merge(idItemOld);
            }
            if (idItemNew != null && !idItemNew.equals(idItemOld))
            {
                idItemNew.getItemVendaList().add(itemVenda);
                idItemNew = em.merge(idItemNew);
            }
            if (idVendaOld != null && !idVendaOld.equals(idVendaNew))
            {
                idVendaOld.getItemVendaList().remove(itemVenda);
                idVendaOld = em.merge(idVendaOld);
            }
            if (idVendaNew != null && !idVendaNew.equals(idVendaOld))
            {
                idVendaNew.getItemVendaList().add(itemVenda);
                idVendaNew = em.merge(idVendaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = itemVenda.getId();
                if (findItemVenda(id) == null)
                {
                    throw new NonexistentEntityException("The itemVenda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemVenda itemVenda;
            try
            {
                itemVenda = em.getReference(ItemVenda.class, id);
                itemVenda.getId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The itemVenda with id " + id + " no longer exists.", enfe);
            }
            Item idItem = itemVenda.getIdItem();
            if (idItem != null)
            {
                idItem.getItemVendaList().remove(itemVenda);
                idItem = em.merge(idItem);
            }
            Venda idVenda = itemVenda.getIdVenda();
            if (idVenda != null)
            {
                idVenda.getItemVendaList().remove(itemVenda);
                idVenda = em.merge(idVenda);
            }
            em.remove(itemVenda);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<ItemVenda> findItemVendaEntities() {
        return findItemVendaEntities(true, -1, -1);
    }

    public List<ItemVenda> findItemVendaEntities(int maxResults, int firstResult) {
        return findItemVendaEntities(false, maxResults, firstResult);
    }

    private List<ItemVenda> findItemVendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemVenda.class));
            Query q = em.createQuery(cq);
            if (!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally
        {
            em.close();
        }
    }

    public ItemVenda findItemVenda(Integer id) {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(ItemVenda.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getItemVendaCount() {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemVenda> rt = cq.from(ItemVenda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
