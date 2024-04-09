package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cliente;
import entidades.Vendedor;
import entidades.ItemVenda;
import entidades.Venda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class VendaJpaController implements Serializable {

    public VendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venda venda) {
        if (venda.getItemVendaList() == null)
        {
            venda.setItemVendaList(new ArrayList<ItemVenda>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = venda.getIdCliente();
            if (idCliente != null)
            {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                venda.setIdCliente(idCliente);
            }
            Vendedor idVendedor = venda.getIdVendedor();
            if (idVendedor != null)
            {
                idVendedor = em.getReference(idVendedor.getClass(), idVendedor.getId());
                venda.setIdVendedor(idVendedor);
            }
            List<ItemVenda> attachedItemVendaList = new ArrayList<ItemVenda>();
            for (ItemVenda itemVendaListItemVendaToAttach : venda.getItemVendaList())
            {
                itemVendaListItemVendaToAttach = em.getReference(itemVendaListItemVendaToAttach.getClass(), itemVendaListItemVendaToAttach.getId());
                attachedItemVendaList.add(itemVendaListItemVendaToAttach);
            }
            venda.setItemVendaList(attachedItemVendaList);
            em.persist(venda);
            if (idCliente != null)
            {
                idCliente.getVendaList().add(venda);
                idCliente = em.merge(idCliente);
            }
            if (idVendedor != null)
            {
                idVendedor.getVendaList().add(venda);
                idVendedor = em.merge(idVendedor);
            }
            for (ItemVenda itemVendaListItemVenda : venda.getItemVendaList())
            {
                Venda oldIdVendaOfItemVendaListItemVenda = itemVendaListItemVenda.getIdVenda();
                itemVendaListItemVenda.setIdVenda(venda);
                itemVendaListItemVenda = em.merge(itemVendaListItemVenda);
                if (oldIdVendaOfItemVendaListItemVenda != null)
                {
                    oldIdVendaOfItemVendaListItemVenda.getItemVendaList().remove(itemVendaListItemVenda);
                    oldIdVendaOfItemVendaListItemVenda = em.merge(oldIdVendaOfItemVendaListItemVenda);
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

    public void edit(Venda venda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Venda persistentVenda = em.find(Venda.class, venda.getId());
            Cliente idClienteOld = persistentVenda.getIdCliente();
            Cliente idClienteNew = venda.getIdCliente();
            Vendedor idVendedorOld = persistentVenda.getIdVendedor();
            Vendedor idVendedorNew = venda.getIdVendedor();
            List<ItemVenda> itemVendaListOld = persistentVenda.getItemVendaList();
            List<ItemVenda> itemVendaListNew = venda.getItemVendaList();
            List<String> illegalOrphanMessages = null;
            for (ItemVenda itemVendaListOldItemVenda : itemVendaListOld)
            {
                if (!itemVendaListNew.contains(itemVendaListOldItemVenda))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemVenda " + itemVendaListOldItemVenda + " since its idVenda field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null)
            {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                venda.setIdCliente(idClienteNew);
            }
            if (idVendedorNew != null)
            {
                idVendedorNew = em.getReference(idVendedorNew.getClass(), idVendedorNew.getId());
                venda.setIdVendedor(idVendedorNew);
            }
            List<ItemVenda> attachedItemVendaListNew = new ArrayList<ItemVenda>();
            for (ItemVenda itemVendaListNewItemVendaToAttach : itemVendaListNew)
            {
                itemVendaListNewItemVendaToAttach = em.getReference(itemVendaListNewItemVendaToAttach.getClass(), itemVendaListNewItemVendaToAttach.getId());
                attachedItemVendaListNew.add(itemVendaListNewItemVendaToAttach);
            }
            itemVendaListNew = attachedItemVendaListNew;
            venda.setItemVendaList(itemVendaListNew);
            venda = em.merge(venda);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew))
            {
                idClienteOld.getVendaList().remove(venda);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld))
            {
                idClienteNew.getVendaList().add(venda);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idVendedorOld != null && !idVendedorOld.equals(idVendedorNew))
            {
                idVendedorOld.getVendaList().remove(venda);
                idVendedorOld = em.merge(idVendedorOld);
            }
            if (idVendedorNew != null && !idVendedorNew.equals(idVendedorOld))
            {
                idVendedorNew.getVendaList().add(venda);
                idVendedorNew = em.merge(idVendedorNew);
            }
            for (ItemVenda itemVendaListNewItemVenda : itemVendaListNew)
            {
                if (!itemVendaListOld.contains(itemVendaListNewItemVenda))
                {
                    Venda oldIdVendaOfItemVendaListNewItemVenda = itemVendaListNewItemVenda.getIdVenda();
                    itemVendaListNewItemVenda.setIdVenda(venda);
                    itemVendaListNewItemVenda = em.merge(itemVendaListNewItemVenda);
                    if (oldIdVendaOfItemVendaListNewItemVenda != null && !oldIdVendaOfItemVendaListNewItemVenda.equals(venda))
                    {
                        oldIdVendaOfItemVendaListNewItemVenda.getItemVendaList().remove(itemVendaListNewItemVenda);
                        oldIdVendaOfItemVendaListNewItemVenda = em.merge(oldIdVendaOfItemVendaListNewItemVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = venda.getId();
                if (findVenda(id) == null)
                {
                    throw new NonexistentEntityException("The venda with id " + id + " no longer exists.");
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
            Venda venda;
            try
            {
                venda = em.getReference(Venda.class, id);
                venda.getId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The venda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ItemVenda> itemVendaListOrphanCheck = venda.getItemVendaList();
            for (ItemVenda itemVendaListOrphanCheckItemVenda : itemVendaListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venda (" + venda + ") cannot be destroyed since the ItemVenda " + itemVendaListOrphanCheckItemVenda + " in its itemVendaList field has a non-nullable idVenda field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = venda.getIdCliente();
            if (idCliente != null)
            {
                idCliente.getVendaList().remove(venda);
                idCliente = em.merge(idCliente);
            }
            Vendedor idVendedor = venda.getIdVendedor();
            if (idVendedor != null)
            {
                idVendedor.getVendaList().remove(venda);
                idVendedor = em.merge(idVendedor);
            }
            em.remove(venda);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Venda> findVendaEntities() {
        return findVendaEntities(true, -1, -1);
    }

    public List<Venda> findVendaEntities(int maxResults, int firstResult) {
        return findVendaEntities(false, maxResults, firstResult);
    }

    private List<Venda> findVendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venda.class));
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

    public Venda findVenda(Integer id) {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Venda.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getVendaCount() {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venda> rt = cq.from(Venda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
