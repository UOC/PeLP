/*
	Copyright 2011-2012 Fundació per a la Universitat Oberta de Catalunya

	This file is part of PeLP (Programming eLearning Plaform).

    PeLP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PeLP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uoc.pelp.bussines;

import edu.uoc.pelp.bussines.exception.*;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.bussines.vo.Classroom;
import edu.uoc.pelp.bussines.vo.Subject;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.DAOPELPEngine;
import edu.uoc.pelp.engine.IPELPEngine;
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.activity.DAOActivityManager;
import edu.uoc.pelp.engine.admin.DAOAdministrationManager;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.engine.deliver.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DAODeliverManager;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverFile.FileType;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.information.DAOInformationManager;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidActivityPelpException;
import edu.uoc.pelp.exception.InvalidSubjectPelpException;
import edu.uoc.pelp.model.dao.*;
import edu.uoc.pelp.model.dao.UOC.SemesterDAO;
import edu.uoc.pelp.model.dao.admin.AdministrationDAO;
import java.io.File;
import org.hibernate.SessionFactory;

/**
 * Implementation of the PELP bussiness functionalities
 * @author Xavier Baró
 */
public abstract class PelpBussinesImpl implements PelpBussines {
    
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
    
    
    /**
     * Default constructor. If all components are instantiated the engine is started.
     */
    public PelpBussinesImpl() throws InvalidEngineException {
        if(_campusConnection!=null && _sessionFactory!=null && _configObject!=null) {
            initializeEngine();
        }
    }
    
    /**
     * Constructor with all the components. If all components are instantiated the engine is started.
     * @param campusConnection Connection to the campus
     * @param sessionFactory Hibernate session factory
     * @param configObject Configuration object
     * @throws InvalidEngineException if there is some problem starting the engine
     */
    public PelpBussinesImpl(ICampusConnection campusConnection,SessionFactory sessionFactory,IPelpConfiguration configObject) throws InvalidEngineException {
        _campusConnection=campusConnection;
        _sessionFactory=sessionFactory;
        _configObject=configObject;
        if(_campusConnection!=null && _sessionFactory!=null && _configObject!=null) {
            initializeEngine();
        }
    }
    
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
                SemesterDAO semesterDAO=new SemesterDAO(sessionFactory);
                LoggingDAO logDAO=new LoggingDAO(sessionFactory);
                StatisticsDAO statsDAO=new StatisticsDAO(sessionFactory);

                // Create the managers
                _engine.setDeliverManager(new DAODeliverManager(deliverDAO,deliverResultsDAO));
                _engine.setActivityManager(new DAOActivityManager(activityDAO));
                _engine.setAdministrationManager(new DAOAdministrationManager(adminDAO,semesterDAO));
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
    public final void initializeEngine() throws InvalidEngineException {
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
    public DeliverDetail compileCode(String code, String programmingLanguage, Test[] tests) throws InvalidEngineException, AuthorizationException, AEMPelpException, ExecPelpException {
       // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Create a new code project
        CodeProject project = new CodeProject(programmingLanguage, code);
        
        // Build test high level objects
        ActivityTest[] givenTests=getTestDataArray(tests,null);
        
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
        ActivityTest[] givenTests=getTestDataArray(tests,null);
        
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

    @Override
    public DeliverDetail[] getUserDeliverDetails(Activity activity) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getUserDeliverDetails(activity.getSubject(),activity.getIndex());
    }

    @Override
    public DeliverDetail addDeliver(Activity activity, DeliverFile[] files) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return addDeliver(activity.getSubject(),activity.getIndex(),files);
    }
    
    @Override
    public Resource[] getActivityResources(Activity activity) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getActivityResources(activity.getSubject(),activity.getIndex());
    }
    
    @Override
    public Activity getActivityInformation(Activity activity) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getActivityInformation(activity.getSubject(),activity.getIndex());
    }

    @Override
    public DeliverSummary[] getAllClassroomDeliverSummary(Activity activity, Classroom classroom) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getAllClassroomDeliverSummary(activity.getSubject(),activity.getIndex(),classroom.getSubject(),classroom.getIndex());
    }
    
    @Override
    public DeliverSummary[] getAllClassroomDeliverSummary(Activity activity, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getAllClassroomDeliverSummary(activity.getSubject(),activity.getIndex(),subject,classIndex);
    }
    
    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(Activity activity, Classroom classroom) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getAllClassroomDeliverDetails(activity.getSubject(),activity.getIndex(),classroom.getSubject(),classroom.getIndex());
    }
    
    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(Activity activity, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getAllClassroomDeliverDetails(activity.getSubject(),activity.getIndex(),subject,classIndex);
    }
    
    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(Activity activity, Classroom classroom) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getLastClassroomDeliverSummary(activity.getSubject(), activity.getIndex(), classroom.getSubject(),classroom.getIndex());
    }
    
    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(Activity activity, Classroom classroom) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getLastClassroomDeliverDetails(activity.getSubject(),activity.getIndex(), classroom.getSubject(),classroom.getIndex());
    }
    
    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(Activity activity, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getLastClassroomDeliverSummary(activity.getSubject(), activity.getIndex(), subject, classIndex);
    }
    
    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(Activity activity, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        return getLastClassroomDeliverDetails(activity.getSubject(),activity.getIndex(), subject,classIndex);
    }
    
    @Override
    public boolean addLaboratory(String mainSubject, String laboratory) throws AuthorizationException, InvalidEngineException {
        boolean retVal=false;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        try {
            retVal=_engine.addLaboratory(mainSubject,laboratory);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retVal;
    }

    @Override
    public boolean removeLaboratory(String mainSubject, String laboratory) throws AuthorizationException, InvalidEngineException {
        boolean retVal=false;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        try {
            retVal=_engine.removeLaboratory(mainSubject,laboratory);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retVal;
    }
    
    @Override
    public abstract Subject getSubject(ISubjectID subjectID);
    
    @Override
    public abstract ISubjectID getSubjectID(Subject subject);
    
    @Override
    public abstract ITimePeriod getSemester(Subject subject);
    
    @Override
    public abstract Classroom getClassroom(IClassroomID classroomID);
    
    @Override
    public abstract IClassroomID getClassroomID(Classroom classroom);
    
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
    
    protected ActivityTest[] getTestDataArray(Test[] tests,MultilingualTextArray[] testDescriptions) throws ExecPelpException {
        // Check the parameters
        if(tests==null) {
            return null;
        }
        if(tests!=null && testDescriptions!=null) {
            if(tests.length!=testDescriptions.length) {
                throw new ExecPelpException("The array of tests and the array of their descriptiona must be of the same length");
            }
        }
        
        // Create the high level test objects
        ActivityTest[] retVal=new ActivityTest[tests.length];
        for(int i=0;i<tests.length;i++) {
            if(testDescriptions!=null) {
                retVal[i]=getTestDataObject(tests[i],testDescriptions[i]);
            } else {
                retVal[i]=getTestDataObject(tests[i],null);
            }
        }
        
        return retVal;
    }
    
    protected ActivityTest getTestDataObject(Test test,MultilingualTextArray descriptions) {
        // Check the parameters
        if(test==null) {
            return null;
        }
        ActivityTest newTest=new ActivityTest();
        if(test.getExpectedOutput()!=null) {
            newTest.setExpectedOutputStr(test.getExpectedOutput());
        }
        if(test.getExpectedOutputFilePath()!=null) {
            newTest.setExpectedOutputFile(new File(test.getExpectedOutputFilePath()));
        }
        if(test.getInputText()!=null) {
            newTest.setInputStr(test.getInputText());
        }
        if(test.getInputFilePath()!=null) {
            newTest.setInputFile(new File(test.getInputFilePath()));
        }
        if(test.getMaxExpectedTime()>0) {
            newTest.setMaxTime((long)test.getMaxExpectedTime());
        }
        newTest.setPublic(test.isPublic());
        
        // Add the descriptions
        if(descriptions!=null) {
            for(MultilingualText desc:descriptions.getArray()) {
                newTest.setDescription(desc.getLanguage(), desc.getText());
            }
        }
        
        return newTest;
    }
    
    protected DeliverDetail getDeliverDetailObject(AnalysisResults analysisResult) throws ExecPelpException, InvalidEngineException {
        DeliverSummary deliverSummary= getDeliverSummaryObject(analysisResult);
        if(deliverSummary==null) {
            return null;
        }
        
        DeliverDetail deliverDetail=new DeliverDetail(deliverSummary);
        
        // No files are provided
        deliverDetail.setDeliverFiles(null);
                
        // Add test information
        edu.uoc.pelp.engine.aem.TestResult[] tests = analysisResult.getResults();
        if(tests!=null) {
            deliverDetail.setTestResults(getTestResultList(tests));
        }
        return deliverDetail;
    }
 
    protected DeliverSummary getDeliverSummaryObject(AnalysisResults analysisResult) throws ExecPelpException, InvalidEngineException {
        
        // Check the parameters
        if(analysisResult==null) {
            return null;
        }
        
        // Create the new object
        DeliverSummary result=new DeliverSummary();
        result.setUser(getUserInformation());
        result.setDeliverIndex(-1);
        result.setProgrammingLanguage(analysisResult.getBuildResult().getLanguage());
        result.setCompileOK(analysisResult.getBuildResult().isCorrect());
        result.setCompileMessage(analysisResult.getBuildResult().getMessge());
        
        // Get the deliver details
        result.setSubmissionDate(analysisResult.getBuildResult().getStartTime());
          
        // Get the maximum test
        result.setMaxDelivers(-1);
        
        // Get test information        
        int passed=0;
        for(edu.uoc.pelp.engine.aem.TestResult testResult:analysisResult.getResults()) {                
            if(testResult.isPassed()) {
                passed++;
            }            
        }
        
        // No private tests
        result.setPassedPrivateTests(0);        
        result.setTotalPrivateTests(0);
        
        result.setPassedPublicTests(passed);
        result.setTotalPublicTests(analysisResult.getResults().length);
        
        return result;
    }
    
    protected DeliverDetail getDeliverDetailObject(Deliver deliver,DeliverResults deliverResult) throws AuthPelpException, InvalidActivityPelpException, InvalidSubjectPelpException, ExecPelpException, InvalidEngineException {
                
        DeliverSummary deliverSummary= getDeliverSummaryObject(deliver,deliverResult);
        if(deliverSummary==null) {
            return null;
        }
        
        DeliverDetail deliverDetail=new DeliverDetail(deliverSummary);
        
        // Add deliver files
        edu.uoc.pelp.engine.deliver.DeliverFile[] files=deliver.getFiles();
        if(files!=null) {
            deliverDetail.setDeliverFiles(getDeliverFileList(deliver.getRootPath(),files));
        }
        // Add test information
        ActivityTestResult[] tests = deliverResult.getResults();
        if(tests!=null) {
            deliverDetail.setTestResults(getTestResultList(tests));
        }
        
        
        return deliverDetail;
    }
        
    protected DeliverSummary getDeliverSummaryObject(Deliver deliver,DeliverResults deliverResult) throws AuthPelpException, ExecPelpException, InvalidEngineException {
        // Check the parameters
        if(deliver==null || deliverResult==null) {
            return null;
        }
        
        // Create the new object
        DeliverSummary result=new DeliverSummary();
        
        //result.setUser(getUserInformation()); // PONER USER ID CON EL IDP QUE TOCA.
        
        Person objPerson = _campusConnection.getUserData(deliver.getID().user);
        result.setUser(getUserInformationObject(objPerson));
        
        result.setDeliverIndex((int)deliverResult.getDeliverID().index);
        result.setProgrammingLanguage(deliverResult.getBuildResults().getLanguage());
        result.setCompileOK(deliverResult.getBuildResults().isCorrect());
        result.setCompileMessage(deliverResult.getBuildResults().getMessge());
        
        // Get the deliver details
        result.setSubmissionDate(deliver.getCreationDate());
          
        // Get the maximum test
        result.setMaxDelivers(_engine.getActivityMaxDelivers(deliverResult.getDeliverID().activity));
        
        // Get test information
        int numPublic=0;
        int numPrivate=0;
        int passedPublic=0;
        int passedPrivate=0;
        for(ActivityTestResult testResult:deliverResult.getResults()) {
            ActivityTest testInfo=_engine.getTestInformation(testResult.getTestID());
            if(testInfo.isPublic()) {
                numPublic++;
                if(testResult.isPassed()) {
                    passedPublic++;
                }
            } else {
                numPrivate++;
                if(testResult.isPassed()) {
                    passedPrivate++;
                }
            }
        }
        result.setPassedPrivateTests(passedPrivate);
        result.setPassedPublicTests(passedPublic);
        result.setTotalPrivateTests(numPrivate);
        result.setTotalPublicTests(numPublic);
        
        return result;
    }
 
    protected UserInformation getUserInformationObject(Person userInfo) {
       
        // Check input parameter
        if(userInfo==null) {
            return null;
        }
        
        // Create the new object
        UserInformation newObj=new UserInformation();
        newObj.setLanguage(userInfo.getLanguage());
        newObj.setUserFullName(userInfo.getFullName());
        newObj.setUserID(userInfo.getUserID().toString());
        newObj.setUserPhoto(userInfo.getUserPhoto());
        newObj.setUsername(userInfo.getUsername());
        newObj.seteMail(userInfo.geteMail());
        
        return newObj;
    }
    
    protected Activity getActivity(edu.uoc.pelp.engine.activity.Activity activity) throws ExecPelpException, InvalidEngineException {
        Activity newObject=new Activity();
        UserInformation userInfo=getUserInformation();
        
        newObject.setDescription(activity.getDescription(userInfo.getLanguage()));
        newObject.setIndex((int)activity.getActivity().index);
        newObject.setSubject(getSubject(activity.getActivity().subjectID));
        
        return newObject;
    }
    
    
    
    protected edu.uoc.pelp.engine.deliver.DeliverFile getDeliverFile(DeliverFile file) throws ExecPelpException {
        // Check the parameters
        if(file==null) {
            return null;
        }
        
        // Get the type of input file
        FileType type=null;
        if(file.isIsCode()) {
            type=FileType.Code;
        } else if(file.isIsReport()) {
            type=FileType.Report;
        }
        
        if(type==null) {
            throw new ExecPelpException("Unrecognized file type");
        }
        // Creat the new file
        edu.uoc.pelp.engine.deliver.DeliverFile newObj=new edu.uoc.pelp.engine.deliver.DeliverFile(new File(file.getRelativePath()),type);
        newObj.setMainProperty(file.isIsMain());
        
        return newObj;
    }
    
    protected Activity[] getActivityList(edu.uoc.pelp.engine.activity.Activity[] activityList) throws ExecPelpException, InvalidEngineException {
        // Check parameters
        if(activityList==null) {
            return null;
        }
        
        // Create the return object
        Activity[] retList=new Activity[activityList.length];
        for(int i=0;i<activityList.length;i++) {
            retList[i]=getActivity(activityList[i]);
        }
        
        return retList;
    }
        
    protected Classroom[] getClassroomList(edu.uoc.pelp.engine.campus.Classroom[] classList) {
        // Check parameters
        if(classList==null) {
            return null;
        }
        
        // Create the return object
        Classroom[] retList=new Classroom[classList.length];
        for(int i=0;i<classList.length;i++) {
            retList[i]=getClassroom(classList[i].getClassroomID());
        }
        
        return retList;
    }
    
    protected DeliverFile[] getDeliverFileList(File rootPath,edu.uoc.pelp.engine.deliver.DeliverFile[] files) {
        DeliverFile[] retList;
        if(files==null) {
            return null;
        }
        retList=new DeliverFile[files.length];
        for(int i=0;i<files.length;i++) {
            retList[i]=getDeliverFile(rootPath,files[i]);
        }
        
        return retList;
    }
    
    protected DeliverFile getDeliverFile(File rootPath,edu.uoc.pelp.engine.deliver.DeliverFile object) {
        
        // Create the output file
        DeliverFile newFile=new DeliverFile(rootPath,object.getRelativePath());
        
        newFile.setIndex((int)object.getID().index);
        switch(object.getType()) {
            case Code:
                newFile.setIsCode(true);
                newFile.setIsMain(object.isMainFile());
                break;
            case Report:
                newFile.setIsReport(true);
                break;
        }

        return newFile;
                

    }

    protected TestResult getTestResult(ActivityTestResult test) throws AuthPelpException {
        
        // Create the new object
        TestResult newResult=new TestResult();
        
        newResult.setIndex((int)test.getTestID().index);
        newResult.setElapsedTime(test.getElapsedTime());
        newResult.setIsPassed(test.isPassed());
        ActivityTest testInfo=_engine.getTestInformation(test.getTestID());
        newResult.setIsPublic(testInfo.isPublic());
        if(testInfo.getExpectedOutputFile()!=null) {
            newResult.setExpectedOutput("<file content>");
        }
        if(testInfo.getExpectedOutputStr()!=null) {
            newResult.setExpectedOutput(testInfo.getExpectedOutputStr());
        }
        newResult.setOutput(test.getOutput());
        
        return newResult;
    }
    
    protected TestResult[] getTestResultList(ActivityTestResult[] tests) throws AuthPelpException {
        TestResult[] retList;
        if(tests==null) {
            return null;
        }
        retList=new TestResult[tests.length];
        for(int i=0;i<tests.length;i++) {
            retList[i]=getTestResult(tests[i]);
        }
        
        return retList;
    }
    
    protected TestResult getTestResult(edu.uoc.pelp.engine.aem.TestResult test) {
        
        // Create the new object
        TestResult newResult=new TestResult();
        
        newResult.setIndex(-1);
        newResult.setElapsedTime(test.getElapsedTime());
        newResult.setIsPassed(test.isPassed());        
        newResult.setIsPublic(true);        
        newResult.setExpectedOutput("<information not provided>");        
        newResult.setOutput(test.getOutput());
        
        return newResult;
    }
    
    protected TestResult[] getTestResultList(edu.uoc.pelp.engine.aem.TestResult[] tests) {
        TestResult[] retList;
        if(tests==null) {
            return null;
        }
        retList=new TestResult[tests.length];
        for(int i=0;i<tests.length;i++) {
            retList[i]=getTestResult(tests[i]);
        }
        
        return retList;
    }
    
    @Override
    public Boolean isAdministrator() throws AuthPelpException {
        return _engine.isAdministrator();
    }
}
