/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package knihovna;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwRezervace;
import knihovna.entity.VwTitul;
import knihovna.entity.VwVypujcka;
import knihovna.entity.Vytisk;

/**
 *
 * @author wmatex
 */
public class DatabaseManager {
    private static DatabaseManager mInstance = null;
    private final EntityManager mEm;
    private final EntityManagerFactory mEmf;
    private static final int PAGE_SIZE = 5;
   
    private DatabaseManager() {
        mEmf = Persistence.createEntityManagerFactory("KnihovnaPU");
        mEm = mEmf.createEntityManager();
        
    }
    
    public static void init() {
        if (DatabaseManager.mInstance == null) {
            DatabaseManager.mInstance = new DatabaseManager();
        }
    }
    
    public static void destroy() {
        if (DatabaseManager.mInstance != null) {
            DatabaseManager.mInstance.mEm.close();
            DatabaseManager.mInstance.mEmf.close();
        }
    }

    public static DatabaseManager getInstance() {
        return DatabaseManager.mInstance;
    }
    
    public Uzivatel login(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String md5Password = md5(password);
        try {
            return mEm.createNamedQuery("Uzivatel.prihlasit", Uzivatel.class)
                .setParameter("email", email)
                .setParameter("heslo", md5Password)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Vytisk getPrintByBarcode(String barcode) {
        try {
            return mEm.createNamedQuery("Vytisk.findByCarKod", Vytisk.class)
                .setParameter("carKod", barcode)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Uzivatel getUserByEmail(String email) {
        try {
            return mEm.createNamedQuery("Uzivatel.findByEmail", Uzivatel.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Collection<VwTitul> searchTitles(String criteria, int pageno) {
        criteria = criteria.trim();
        criteria = criteria.replaceAll("\\s+", "|");
        return mEm.createNamedQuery("VwTitul.searchForTitul", VwTitul.class)
            .setParameter(1, criteria)
            .setParameter(2, criteria)
            .setParameter(3, PAGE_SIZE)
            .setParameter(4, pageno*PAGE_SIZE)
            .getResultList();
    }

    public String createUser(String name, String surname, String email) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("Uzivatel.novy");
        query.setParameter("_jmeno", name);
        query.setParameter("_prijmeni", surname);
        query.setParameter("_email", email);

        String password = null;
        try {
            query.execute();
            password = (String) query.getOutputParameterValue(4);
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
            throw new EntityExistsException();
        }
        return password;
    }

    public void createReservation(Uzivatel user, VwTitul title) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("VwRezervace.nova");
        query.setParameter("_id_uzivatel", user.getIdUzivatel());
        query.setParameter("_id_titul", title.getIdTitul());
        query.setParameter("_interval", null);

        query.execute();
    }

    public void createBorrowing(Vytisk print, Uzivatel user) {
        VwVypujcka borrowing = new VwVypujcka(print.getIdVytisk(), user.getIdUzivatel());
        EntityTransaction transaction = mEm.getTransaction();
        try {
            transaction.begin();
            mEm.persist(borrowing);
            transaction.commit();
        } catch (PersistenceException ex) {
            throw new EntityExistsException();
        }
    }

    public void returnBorrowing(VwVypujcka borrowing) {
        EntityTransaction transaction = mEm.getTransaction();
        transaction.begin();
        borrowing.setJeVraceno(true);
        mEm.persist(borrowing);
        transaction.commit();
    }

    public List<VwRezervace> getWaitingRezervations(int pageno) {
        return mEm.createNamedQuery("VwRezervace.findWaiting", VwRezervace.class)
            .setParameter(1, PAGE_SIZE)
            .setParameter(2, pageno*PAGE_SIZE)
            .getResultList();
    }

    public List<Uzivatel> getUsers() {
        return mEm.createNamedQuery("Uzivatel.getAll", Uzivatel.class)
            .getResultList();
    }

    public List<VwVypujcka> getBorrowsOfUser(Uzivatel user) {
        return mEm.createNamedQuery("Vypujcka.findByUser", VwVypujcka.class)
            .setParameter("uzivatel", user.getIdUzivatel())
            .getResultList();
    }
    public List<VwRezervace> getReservationsOfUser(Uzivatel user, int pageno) {
        return mEm.createNamedQuery("VwRezervace.findByUser", VwRezervace.class)
            .setParameter(1, user.getIdUzivatel())
            .setParameter(2, PAGE_SIZE)
            .setParameter(3, pageno*PAGE_SIZE)
            .getResultList();
    }
    
    public VwVypujcka getBorrowByVytisk(Vytisk print){
        try {
            return mEm.createNamedQuery("Vypujcka.findByVytisk", VwVypujcka.class)
                .setParameter("id", print.getIdVytisk())
                .getSingleResult();
        } catch (NoResultException ex){
            return null;
        }
        
    }
    
    public static String md5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes("UTF-8"));
        byte[] bytes = md.digest();
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    
    
}
