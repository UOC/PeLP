SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `pelp` ;
CREATE SCHEMA IF NOT EXISTS `pelp` DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish2_ci ;
USE `pelp` ;

-- -----------------------------------------------------
-- Table `pelp`.`semester`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`semester` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`semester` (
  `semester` CHAR(5) CHARACTER SET 'utf8' COLLATE 'utf8_spanish2_ci' NOT NULL ,
  `startDate` DATETIME NULL DEFAULT NULL ,
  `endDate` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`semester`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_spanish2_ci
COMMENT = 'Table where semester information is stored';


-- -----------------------------------------------------
-- Table `pelp`.`activity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`activity` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`activity` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `startDate` DATETIME NULL ,
  `endDate` DATETIME NULL ,
  `maxDelivers` INT(11) NULL ,
  `progLanguage` VARCHAR(15) NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`PELP_Languages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`PELP_Languages` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`PELP_Languages` (
  `langCode` CHAR(15) NOT NULL ,
  `langDesc` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`langCode`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`activityDesc`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`activityDesc` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`activityDesc` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `langCode` CHAR(15) NOT NULL ,
  `description` LONGTEXT NOT NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `langCode`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`activityTest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`activityTest` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`activityTest` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `testIndex` INT NOT NULL ,
  `publicTest` BIT NULL ,
  `strInput` VARCHAR(1024) NULL ,
  `strOutput` VARCHAR(1024) NULL ,
  `fileInput` VARCHAR(255) NULL ,
  `fileOutput` VARCHAR(255) NULL ,
  `maxTime` BIGINT NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `testIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`deliver`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`deliver` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`deliver` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `userID` CHAR(25) NOT NULL ,
  `deliverIndex` INT NOT NULL ,
  `rootPath` VARCHAR(512) NOT NULL ,
  `submissionDate` DATETIME NOT NULL ,
  `classroom` VARCHAR(45) NULL ,
  `laboratory` VARCHAR(45) NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `userID`, `deliverIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`deliverFile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`deliverFile` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`deliverFile` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `userID` CHAR(25) NOT NULL ,
  `deliverIndex` INT NOT NULL ,
  `fileIndex` INT NOT NULL ,
  `relativePath` VARCHAR(512) NOT NULL ,
  `type` VARCHAR(15) NOT NULL ,
  `main` BIT NOT NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `userID`, `deliverIndex`, `fileIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`deliverResult`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`deliverResult` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`deliverResult` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `userID` CHAR(25) NOT NULL ,
  `deliverIndex` INT NOT NULL ,
  `compilation` BIT NOT NULL ,
  `compMessage` LONGTEXT NULL ,
  `execution` BIT NOT NULL ,
  `progLanguage` VARCHAR(15) NOT NULL ,
  `startDate` DATETIME NULL ,
  `endDate` DATETIME NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `userID`, `deliverIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`deliverTestResult`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`deliverTestResult` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`deliverTestResult` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `userID` CHAR(25) NOT NULL ,
  `deliverIndex` INT NOT NULL ,
  `testIndex` INT NOT NULL ,
  `passed` BIT NOT NULL ,
  `programOutput` LONGTEXT NULL ,
  `elapsedTime` BIGINT NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `userID`, `deliverIndex`, `testIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`PELP_activeSubjects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`PELP_activeSubjects` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`PELP_activeSubjects` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `active` BIT NULL ,
  PRIMARY KEY (`semester`, `subject`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`PELP_mainLabSubjects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`PELP_mainLabSubjects` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`PELP_mainLabSubjects` (
  `mainSubjectCode` CHAR(6) NOT NULL ,
  `labSubjectCode` CHAR(6) NOT NULL ,
  PRIMARY KEY (`mainSubjectCode`, `labSubjectCode`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`PELP_Admins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`PELP_Admins` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`PELP_Admins` (
  `userName` CHAR(25) NOT NULL ,
  `active` BIT NULL ,
  `grantAllowed` BIT NULL ,
  PRIMARY KEY (`userName`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`testDesc`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`testDesc` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`testDesc` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `testIndex` INT NOT NULL ,
  `langCode` CHAR(15) NOT NULL ,
  `description` LONGTEXT NOT NULL ,
  `activityDesc` VARCHAR(45) NULL ,
  PRIMARY KEY (`semester`, `subject`, `testIndex`, `langCode`, `activityIndex`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`resource`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`resource` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`resource` (
  `resourceID` BIGINT NOT NULL ,
  `resourceType` VARCHAR(45) NULL ,
  PRIMARY KEY (`resourceID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`resource_URL_Files`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`resource_URL_Files` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`resource_URL_Files` (
  `resourceID` BIGINT NOT NULL ,
  `langCode` VARCHAR(15) NOT NULL ,
  `type` CHAR(10) NOT NULL COMMENT 'File/URL' ,
  `path` VARCHAR(128) NOT NULL ,
  PRIMARY KEY (`resourceID`, `langCode`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`activityResource`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`activityResource` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`activityResource` (
  `semester` CHAR(5) NOT NULL ,
  `subject` CHAR(6) NOT NULL ,
  `activityIndex` INT NOT NULL ,
  `resourceID` BIGINT NOT NULL ,
  PRIMARY KEY (`semester`, `subject`, `activityIndex`, `resourceID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pelp`.`PELP_ErrorLog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pelp`.`PELP_ErrorLog` ;

CREATE  TABLE IF NOT EXISTS `pelp`.`PELP_ErrorLog` (
  `timestamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
  `userIP` VARCHAR(45) NULL ,
  `userInfo` VARCHAR(45) NULL ,
  `errorID` VARCHAR(45) NULL ,
  `message` VARCHAR(1024) NULL )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
