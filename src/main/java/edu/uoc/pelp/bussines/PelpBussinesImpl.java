package edu.uoc.pelp.bussines;

import edu.uoc.pelp.bussines.exception.*;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.DAOPELPEngine;
import edu.uoc.pelp.engine.IPELPEngine;
import edu.uoc.pelp.engine.activity.DAOActivityManager;
import edu.uoc.pelp.engine.admin.DAOAdministrationManager;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.TestData;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.engine.deliver.DAODeliverManager;
import edu.uoc.pelp.engine.information.DAOInformationManager;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.model.dao.*;
import edu.uoc.pelp.model.dao.admin.AdministrationDAO;
import java.io.File;
import org.hibernate.SessionFactory;

/**
 * Implementation of the PELP bussiness functionalities
 * @author Xavier Baró
 */
public class PelpBussinesImpl implements PelpBussines {
    
    /**
     * Campus connection object
     */
    protected ICampusConnection _campusConnection;
    
    /**
     * Session factory for DAO support
     */
    protected SessionFactory _sessionFactory;
    
    /**
     * Configuration object
     */
    protected IPelpConfiguration _configObject;
    
    /**
     * PeLP engine that allows to comunicate with the platform
     */
    protected IPELPEngine _engine;
    
    
    @Override
    public void setCampusConnection(ICampusConnection campusConnection) throws InvalidCampusConnectionException {
        // Check the campus connection object
        if(campusConnection==null) {
            throw new InvalidCampusConnectionException("Null campus connection is detected");
        }
        // Store new object object
        _campusConnection=campusConnection;
        // Update the engine
        if(_engine!=null) {
            _engine.setCampusConnection(campusConnection);
        }
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) throws InvalidSessionFactoryException { 
        // Check the session factory object
        if(sessionFactory==null) {
            throw new InvalidSessionFactoryException("Null session factory is detected");
        }
        // Store new object object
        _sessionFactory=sessionFactory;
        // Update the engine
        if(_engine!=null) {
            if(_engine instanceof DAOPELPEngine) {
                ((DAOPELPEngine)_engine).updateSessionFactory(sessionFactory);
            } else {
                // Create the DAO objects
                ActivityDAO activityDAO=new ActivityDAO(sessionFactory);
                DeliverDAO deliverDAO=new DeliverDAO(sessionFactory);
                DeliverResultsDAO deliverResultsDAO=new DeliverResultsDAO(sessionFactory);        
                AdministrationDAO adminDAO=new AdministrationDAO(sessionFactory);
                LoggingDAO logDAO=new LoggingDAO(sessionFactory);
                StatisticsDAO statsDAO=new StatisticsDAO(sessionFactory);

                // Create the managers
                _engine.setDeliverManager(new DAODeliverManager(deliverDAO,deliverResultsDAO));
                _engine.setActivityManager(new DAOActivityManager(activityDAO));
                _engine.setAdministrationManager(new DAOAdministrationManager(adminDAO));
                _engine.setInformationManager(new DAOInformationManager(logDAO,statsDAO));
            }
        }
    }

    @Override
    public void setConfiguration(IPelpConfiguration config) throws InvalidConfigurationException {
        if(config==null) {
            throw new InvalidConfigurationException("Null configuration object detected");
        }
        _configObject=config;
    }

    @Override
    public void initializeEngine() throws InvalidEngineException {
        if(_sessionFactory==null) {
            throw new InvalidEngineException("Null session factory object found during engine initialization.");
        }
        if(_campusConnection==null) {
            throw new InvalidEngineException("Null campus connection object found during engine initialization.");
        }
        if(_configObject==null) {
            throw new InvalidEngineException("Null configuration object found during engine initialization.");
        }
        
        // Create the engine object
        _engine=new DAOPELPEngine(_sessionFactory,_campusConnection,_configObject);
        
        // Check the result
        if(_engine==null) {
            throw new InvalidEngineException("Unknown error on engine initialization.");
        }
    }

    @Override
    public DeliverDetail compileCode(String code, String programmingLanguage, Test[] tests) throws InvalidEngineException, AuthorizationException, AEMPelpException {
       // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Create a new code project
        CodeProject project = new CodeProject(programmingLanguage, code);
        
        // Build test high level objects
        TestData[] givenTests=getTestDataArray(tests);
        
        // Analyze the project
        AnalysisResults result=_engine.analyzeCode(project, givenTests);
        
        // Create the output object and get it back        
        return getDeliverDetailObject(result);
    }

    @Override
    public DeliverDetail compileCode(DeliverFile[] codeFiles, String programmingLanguage, Test[] tests, String rootPath) throws ExecPelpException,InvalidEngineException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // If no root path is given, estimate it from files
        if(rootPath==null) {
            rootPath=getMaxCommonPath(codeFiles);
        }
        
        // Create a new code project with only code files
        CodeProject project = new CodeProject(new File(rootPath));
        project.setLanguage(programmingLanguage);
        for(DeliverFile f:codeFiles) {
            File newFile=new File(f.getAbsolutePath());
            if(f.isIsCode() && f.isIsMain()) {
                project.addMainFile(newFile);
            } else if(f.isIsCode()){
                project.addFile(newFile);
            }
        }
        
        // Build test high level objects
        TestData[] givenTests=getTestDataArray(tests);
        
        // Analyze the project
        AnalysisResults result=_engine.analyzeCode(project, givenTests);
                
        // Create the output object and get it back        
        return getDeliverDetailObject(result);
    }

    @Override
    public UserInformation getUserInformation() throws ExecPelpException,InvalidEngineException {
        Person userInfo=null;
        try {
            // Check the engine
            if(_engine==null) {
                throw new InvalidEngineException("Uninitialized engine.");
            }
            
            // Get the user information
            if(_engine.isUserAuthenticated()) {
                userInfo=_engine.getUserInfo();
            }
        } catch (AuthPelpException ex) {
            throw new ExecPelpException("Cannot verify user authentication information:\n" + ex.getMessage());
        }
        
        return getUserInformationObject(userInfo);
    }

    @Override
    public Resource getResource(String code) throws InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Get the resource
        throw new UnsupportedOperationException("Not supported yet.");
    } 
    
    protected String getMaxCommonPath(DeliverFile[] codeFiles) {
        String commonPath=null;
        
        // Check file paths
        for(DeliverFile f:codeFiles) {
            if(f.isIsCode()) {
                // Get the path for new file
                String newPath=f.getAbsolutePath();
                
                // Compare with old common path
                if(commonPath==null) {
                    commonPath=newPath;
                } else {
                    // Get common parts between two paths
                    int commonLen=0;
                    while(commonLen<commonPath.length() && commonPath.charAt(commonLen)==newPath.charAt(commonLen)) {
                        commonLen++;
                    }
                    
                    // In case of no coincidence, return null
                    if(commonLen==0) {
                        return null;
                    }
                    
                    // Reduce the common path
                    commonPath=commonPath.substring(0, commonLen);
                }
            }
        }
        
        return commonPath;
    }

    protected TestData getTestDataObject(Test test) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected TestData[] getTestDataArray(Test[] test) {
        TestData[] retVal=null;
        if(test!=null) {
            retVal=new TestData[test.length];
            for(int i=0;i<test.length;i++) {
                retVal[i]=getTestDataObject(test[i]);
            }
        }
        return retVal;
    }
   
    protected DeliverDetail getDeliverDetailObject(AnalysisResults analysisResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    protected DeliverSummary getDeliverSummaryObject(AnalysisResults analysisResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    protected UserInformation getUserInformationObject(Person userInfo) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
