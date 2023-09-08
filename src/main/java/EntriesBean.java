import org.primefaces.PrimeFaces;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@ApplicationScoped
public class EntriesBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String persistenceUnit = "StudsPU";

    private List<Entry> list;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;


    public EntriesBean(){
        list = new ArrayList<>();

        connection();
        loadEntries();
    }

    private void connection() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
    }


    private void loadEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("FROM Entry");
            list = query.getResultList();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }


    public void addEntry(Entry entry) {
        try {
            transaction.begin();
            entityManager.persist(entry);
            list.add(entry);
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    public List<Entry> getList() {
        return list;
    }

    public void setList(List<Entry> list) {
        this.list = list;
    }

    public void addEntryWithParameters(Entry entry){
        try {
            transaction.begin();
            entityManager.persist(entry);
            list.add(entry);
            PrimeFaces.current().ajax().addCallbackParam("verdict", entry.getVerdict());

            transaction.commit();
        } catch (RuntimeException exception) {
            System.out.println("error:" + exception.getMessage());
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw exception;
        }
    }

}
