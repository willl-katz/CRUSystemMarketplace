package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Venda;
import entidades.Vendedor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class VendedorJpaController implements Serializable {

    public VendedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vendedor vendedor) {
        if (vendedor.getVendaList() == null)
        {
            vendedor.setVendaList(new ArrayList<Venda>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Venda> attachedVendaList = new ArrayList<Venda>();
            for (Venda vendaListVendaToAttach : vendedor.getVendaList())
            {
                vendaListVendaToAttach = em.getReference(vendaListVendaToAttach.getClass(), vendaListVendaToAttach.getId());
                attachedVendaList.add(vendaListVendaToAttach);
            }
            vendedor.setVendaList(attachedVendaList);
            em.persist(vendedor);
            for (Venda vendaListVenda : vendedor.getVendaList())
            {
                Vendedor oldIdVendedorOfVendaListVenda = vendaListVenda.getIdVendedor();
                vendaListVenda.setIdVendedor(vendedor);
                vendaListVenda = em.merge(vendaListVenda);
                if (oldIdVendedorOfVendaListVenda != null)
                {
                    oldIdVendedorOfVendaListVenda.getVendaList().remove(vendaListVenda);
                    oldIdVendedorOfVendaListVenda = em.merge(oldIdVendedorOfVendaListVenda);
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

    public void edit(Vendedor vendedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Vendedor persistentVendedor = em.find(Vendedor.class, vendedor.getId());
            List<Venda> vendaListOld = persistentVendedor.getVendaList();
            List<Venda> vendaListNew = vendedor.getVendaList();
            List<String> illegalOrphanMessages = null;
            for (Venda vendaListOldVenda : vendaListOld)
            {
                if (!vendaListNew.contains(vendaListOldVenda))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venda " + vendaListOldVenda + " since its idVendedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Venda> attachedVendaListNew = new ArrayList<Venda>();
            if(vendaListNew != null)
            for (Venda vendaListNewVendaToAttach : vendaListNew)
            {
                vendaListNewVendaToAttach = em.getReference(vendaListNewVendaToAttach.getClass(), vendaListNewVendaToAttach.getId());
                attachedVendaListNew.add(vendaListNewVendaToAttach);
            }
            vendaListNew = attachedVendaListNew;
            vendedor.setVendaList(vendaListNew);
            vendedor = em.merge(vendedor);
            for (Venda vendaListNewVenda : vendaListNew)
            {
                if (!vendaListOld.contains(vendaListNewVenda))
                {
                    Vendedor oldIdVendedorOfVendaListNewVenda = vendaListNewVenda.getIdVendedor();
                    vendaListNewVenda.setIdVendedor(vendedor);
                    vendaListNewVenda = em.merge(vendaListNewVenda);
                    if (oldIdVendedorOfVendaListNewVenda != null && !oldIdVendedorOfVendaListNewVenda.equals(vendedor))
                    {
                        oldIdVendedorOfVendaListNewVenda.getVendaList().remove(vendaListNewVenda);
                        oldIdVendedorOfVendaListNewVenda = em.merge(oldIdVendedorOfVendaListNewVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = vendedor.getId();
                if (findVendedor(id) == null)
                {
                    throw new NonexistentEntityException("The vendedor with id " + id + " no longer exists.");
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
            Vendedor vendedor;
            try
            {
                vendedor = em.getReference(Vendedor.class, id);
                vendedor.getId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The vendedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Venda> vendaListOrphanCheck = vendedor.getVendaList();
            for (Venda vendaListOrphanCheckVenda : vendaListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vendedor (" + vendedor + ") cannot be destroyed since the Venda " + vendaListOrphanCheckVenda + " in its vendaList field has a non-nullable idVendedor field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vendedor);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Vendedor> findVendedorEntities() {
        return findVendedorEntities(true, -1, -1);
    }

    public List<Vendedor> findVendedorEntities(int maxResults, int firstResult) {
        return findVendedorEntities(false, maxResults, firstResult);
    }

    private List<Vendedor> findVendedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vendedor.class));
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

    public Vendedor findVendedor(Integer id) {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Vendedor.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getVendedorCount() {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vendedor> rt = cq.from(Vendedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
