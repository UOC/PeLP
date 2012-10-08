package edu.uoc.pelp.bussines;

import edu.uoc.pelp.bussines.exception.*;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.exception.ExecPelpException;
import org.hibernate.SessionFactory;

/**
 * PeLP bussines interface, that defines the interaction between services and the platform
 * @author Xavier Baró
 */
public interface PelpBussines {
    
    /**
     * Assigns a new campus connection
     * @param campusConnection Campus connection object
     */
    public void setCampusConnection(ICampusConnection campusConnection) throws InvalidCampusConnectionException;
    
    /**
     * Assigns a new Session Factory for DAO support
     * @param sessionFactory Session factory object
     * @throws InvalidSessionFactoryException If an invalid Session Factory is detected
     */
    public void setSessionFactory(SessionFactory sessionFactory) throws InvalidSessionFactoryException;
    
    /**
     * Assigns a new Configuration object for the PeLP platform
     * @param config Configuration Object
     * @throws InvalidSessionFactoryException If an invalid Configuration is detected
     */
    public void setConfiguration(IPelpConfiguration config) throws InvalidConfigurationException;
    
    /**
     * Create and configure the PeLP engine
     * @throws InvalidEngineException if an error occurs creating the engine
     */
    public void initializeEngine() throws InvalidEngineException;
    
        /**
     * Compile a code and test it with given tests
     * @param code String with the code
     * @param programmingLanguage Programming language code for given code
     * @param tests Set of tests to be passed to the created application
     * @return Object with the detail of given code or null if an error occurred.
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if the user has no enough privilegies
     */
    public DeliverDetail compileCode(String code,String programmingLanguage, Test[] tests) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Compile a code files and test it with given tests
     * @param codeFiles Code files to be analyzed
     * @param programmingLanguage Programming language code for given code
     * @param tests Set of tests to be passed to the created application
     * @param rootPath Set the source rootpath. All files are taked relative to this root path. If null, this path is estimated from the files.
     * @return Object with the detail of given code or null if an error occurred.
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if the user has no enough privilegies
     */
    public DeliverDetail compileCode(DeliverFile[] codeFiles,String programmingLanguage, Test[] tests, String rootPath) throws ExecPelpException,InvalidEngineException;
    
    /**
     * Get the current user information or null if it is not logged in
     * @return User information or null if no user is authenticated
     * @throws AuthorizationException if an error occurs accessing authentication information
     * @throws InvalidEngineException if the engine is not properly initialized
     */
    public UserInformation getUserInformation() throws ExecPelpException,InvalidEngineException;
    
    /**
     * Get a resource the platform
     * @param code Code for the resource
     * @return Resource information or null if it is not accessible or does not exist
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if the user has no access to this resource
     */
    public Resource getResource(String code) throws ExecPelpException,InvalidEngineException,AuthorizationException;
}

