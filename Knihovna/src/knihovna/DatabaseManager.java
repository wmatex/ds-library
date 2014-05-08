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
import javax.persistence.RollbackException;
import knihovna.entity.Uzivatel;
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
    private static final int PAGE_SIZE = 10;
    
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

    public Collection<VwTitul> searchTitles(String name, int pageno) {
        return mEm.createNamedQuery("VwTitul.searchForTitul", VwTitul.class)
            .setParameter(1, name)
            .setParameter(2, PAGE_SIZE)
            .setParameter(3, pageno*PAGE_SIZE)
            .getResultList();
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

    public List<Uzivatel> getUsers() {
        return mEm.createNamedQuery("Uzivatel.getAll", Uzivatel.class)
            .getResultList();
    }

    public List<VwVypujcka> getBorrowsOfUser(Uzivatel user) {
        return mEm.createNamedQuery("Vypujcka.findByUser", VwVypujcka.class)
            .setParameter("uzivatel", user.getIdUzivatel())
            .getResultList();
    }
    
    private String md5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
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
