package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import entidades.Item;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ItemVenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        if (item.getItemVendaList() == null)
        {
            item.setItemVendaList(new ArrayList<ItemVenda>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ItemVenda> attachedItemVendaList = new ArrayList<ItemVenda>();
            for (ItemVenda itemVendaListItemVendaToAttach : item.getItemVendaList())
            {
                itemVendaListItemVendaToAttach = em.getReference(itemVendaListItemVendaToAttach.getClass(), itemVendaListItemVendaToAttach.getId());
                attachedItemVendaList.add(itemVendaListItemVendaToAttach);
            }
            item.setItemVendaList(attachedItemVendaList);
            em.persist(item);
            for (ItemVenda itemVendaListItemVenda : item.getItemVendaList())
            {
                Item oldIdItemOfItemVendaListItemVenda = itemVendaListItemVenda.getIdItem();
                itemVendaListItemVenda.setIdItem(item);
                itemVendaListItemVenda = em.merge(itemVendaListItemVenda);
                if (oldIdItemOfItemVendaListItemVenda != null)
                {
                    oldIdItemOfItemVendaListItemVenda.getItemVendaList().remove(itemVendaListItemVenda);
                    oldIdItemOfItemVendaListItemVenda = em.merge(oldIdItemOfItemVendaListItemVenda);
                }
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

    public void edit(Item item) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getId());
            List<ItemVenda> itemVendaListOld = persistentItem.getItemVendaList();
            List<ItemVenda> itemVendaListNew = item.getItemVendaList();
            List<String> illegalOrphanMessages = null;
            for (ItemVenda itemVendaListOldItemVenda : itemVendaListOld)
            {
                if (!itemVendaListNew.contains(itemVendaListOldItemVenda))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemVenda " + itemVendaListOldItemVenda + " since its idItem field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ItemVenda> attachedItemVendaListNew = new ArrayList<ItemVenda>();
            if(itemVendaListNew != null)
            for (ItemVenda itemVendaListNewItemVendaToAttach : itemVendaListNew)
            {
                itemVendaListNewItemVendaToAttach = em.getReference(itemVendaListNewItemVendaToAttach.getClass(), itemVendaListNewItemVendaToAttach.getId());
                attachedItemVendaListNew.add(itemVendaListNewItemVendaToAttach);
            }
            itemVendaListNew = attachedItemVendaListNew;
            item.setItemVendaList(itemVendaListNew);
            item = em.merge(item);
            for (ItemVenda itemVendaListNewItemVenda : itemVendaListNew)
            {
                if (!itemVendaListOld.contains(itemVendaListNewItemVenda))
                {
                    Item oldIdItemOfItemVendaListNewItemVenda = itemVendaListNewItemVenda.getIdItem();
                    itemVendaListNewItemVenda.setIdItem(item);
                    itemVendaListNewItemVenda = em.merge(itemVendaListNewItemVenda);
                    if (oldIdItemOfItemVendaListNewItemVenda != null && !oldIdItemOfItemVendaListNewItemVenda.equals(item))
                    {
                        oldIdItemOfItemVendaListNewItemVenda.getItemVendaList().remove(itemVendaListNewItemVenda);
                        oldIdItemOfItemVendaListNewItemVenda = em.merge(oldIdItemOfItemVendaListNewItemVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = item.getId();
                if (findItem(id) == null)
                {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Item item;
            try
            {
                item = em.getReference(Item.class, id);
                item.getId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ItemVenda> itemVendaListOrphanCheck = item.getItemVendaList();
            for (ItemVenda itemVendaListOrphanCheckItemVenda : itemVendaListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the ItemVenda " + itemVendaListOrphanCheckItemVenda + " in its itemVendaList field has a non-nullable idItem field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try{
            return em.find(Item.class, id);
        } catch (Exception e){
            System.out.println(e.getClass());
            return null;
        }finally{
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
