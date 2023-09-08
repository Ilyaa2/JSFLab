
import org.primefaces.PrimeFaces;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.Map;

@javax.faces.bean.ManagedBean(name = "entryBean")
@SessionScoped
public class EntryBean implements Serializable {
    private static final long serialVersionUID = 2L;

    @ManagedProperty("#{entriesBean}")
    private EntriesBean entriesBean = new EntriesBean();

    private Entry entry;

    public EntryBean(){
        entry = new Entry();
    }

    public EntriesBean getEntriesBean() {
        return entriesBean;
    }

    public void setEntriesBean(EntriesBean entriesBean) {
        this.entriesBean = entriesBean;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public void addEntry() {
        entry.setVerdict(entry.processVerdict(entry.getX(), entry.getY(), entry.getR()));
        entriesBean.addEntry(entry);
        entry = new Entry();
    }

    public void addEntryWithParameters() {
        if (entry == null) entry = new Entry();
        Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        entry.setX(Integer.parseInt(paramMap.get("x")));
        entry.setY(Double.parseDouble(paramMap.get("y")));
        entry.setR(Double.parseDouble(paramMap.get("r")));
        entry.setVerdict(entry.processVerdict(entry.getX(), entry.getY(), entry.getR()));
        System.out.println(entry);

        entriesBean.addEntryWithParameters(entry);
        entry = new Entry();
    }

    public void sendVerdict(){
        PrimeFaces.current().ajax().addCallbackParam("verdict", entry.processVerdict(entry.getX(), entry.getY(), entry.getR()));
        entry = new Entry();
    }
}
