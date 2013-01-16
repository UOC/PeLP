package edu.uoc.pelp.servlets.UOC;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;

/**
 * Class that encodes campus connection information in the session. 
 * @author Xavier Baró
 */
public class SessionTokenKey {    
    /**
     * UOC Token that enables campus query
     */
    protected String tokenKey;
    
    /**
     * IP of current user to avoid user suplantation
     */
    protected String userIP;
    
    /**
     * Digest of class data to ensure that information is not altered
     */
    protected byte[] signature;
    
    /**
     * Default constructor. Stores data and sign it
     */
    public SessionTokenKey(String secretKey,String userIP,String tokenKey) {
        this.userIP=userIP;
        this.tokenKey=tokenKey;
        signature=createSignature(secretKey);
    }
    
    /**
     * Creates the signature of the object
     * @param Get the secret key to initialize the signature object
     * @return Signature for current data
     */
    private byte[] createSignature(String secretKey) {
        try {
            // Check given key
            if(secretKey==null) {
                return null;
            }
            
            // Create a key pair using secretKey as random seed.
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(secretKey.getBytes());
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();
                    
            // Create a signature object and initialize with secret key
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");             
            dsa.initSign(priv);
            
            // Sign current object
            dsa.update(tokenKey.getBytes());
            dsa.update(userIP.getBytes());
            
            // Return the signature
            return dsa.sign();
            
        } catch (SignatureException ex) {
            return null;
        } catch (InvalidKeyException ex) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (NoSuchProviderException ex) {
            return null;
        }
    }

    /**
     * Check if the current object is valid.
     * @param secretKey Secret key for the application
     * @return True if current content is valid or false otherwise
     */
    public boolean isValid(String secretKey) {
        if(signature==null) {
            return false;
        }
        
        return Arrays.equals(signature, createSignature(secretKey));
    }

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}
}