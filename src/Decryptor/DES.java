package Decryptor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.ArrayList;
import java.util.Random;
import java.security.SecureRandom;

public class DES {

    // private variable for key memorization
    private  byte[] skey = new byte[1000];
    private  String skeyString;
    private  byte[] raw;

    // encrypted password variable
    private ArrayList<byte []> encryptsPss;
    private ArrayList<Boolean> areDecripted;

    public DES() {

        try {

            generateSymmetricKey();

        } catch (Exception e) {
            System.out.println("Error in symmetric key cration");
            System.err.println(e);
        }

    }

    public void setPasswords(ArrayList<String> passwords) {
        encryptsPss = new ArrayList<>();
        areDecripted = new ArrayList<>();

        try{

            for (String p : passwords) {
                // encrypt the password to find
                byte [] pss = p.getBytes();
                encryptsPss.add(encryt(pss));
                areDecripted.add(false);
            }

        }catch (Exception e){
            System.out.println("Error in symmetric key cration");
            System.err.println(e);
        }

    }


    private void generateSymmetricKey() throws Exception {
        /*
            Create the symmetric key and encrypt password
         */

        // define random namber to simmetric key
        Random r = new Random();
        int num = r.nextInt(10000);

        // convert it to string and take the raw key
        String knum = String.valueOf(num);
        byte [] knumb = knum.getBytes();
        skey = getRawKey(knumb);

        // create the DES symmetric key
        this.skeyString = new String(skey);
        //System.out.println("DES Symmetric key = " + skeyString);

    }

    private byte[] getRawKey(byte[] seed) throws Exception {
        /*
            Create the raw key
         */

        // key generation
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(56, sr);
        SecretKey skey = kgen.generateKey();
        raw = skey.getEncoded();

        return raw;
    }

    public byte[] encryt(byte[] clear) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "DES");
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte [] encrypt = cipher.doFinal(clear);

        return encrypt;
    }

    public boolean checkPss(byte [] clear) {

        if (encryptsPss.contains(clear)){
            areDecripted.set(encryptsPss.indexOf(clear), true);
            return true;
        }

        return false;
    }

    public boolean checkEqual() { return areDecripted.stream().allMatch(f -> f == true); }

}
