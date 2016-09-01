USE DoctorOffice

IF OBJECT_ID('spGetDoctors') IS NOT NULL
	DROP PROC spGetDoctors

GO 
CREATE PROC spGetDoctors
AS 
SELECT DISTINCT visit.DoctorID [PersonID], per.FirstName + ' ' + per.LastName [Doctor's Name] -- can get rid of doc id
FROM dbo.OfficeVisit [visit] INNER JOIN dbo.Person [per]
ON visit.DoctorID = per.PersonID;

EXEC spGetDoctors
--------------------------------------------------------------------------------
IF OBJECT_ID('spGetDocPatients') IS NOT NULL
	DROP PROC spGetDocPatients

GO 
CREATE PROC spGetDocPatients
	@doctorID int
AS
SELECT DISTINCT patient.FirstName + ' ' + patient.LastName [Patient Name]
	, doc.FirstName + ' ' + doc.LastName [Doctor's Name] -- get rid of later
FROM dbo.OfficeVisit [visit] INNER JOIN dbo.Person [patient]
ON visit.PatientID = patient.PersonID
INNER JOIN dbo.Person [doc]
ON visit.DoctorID = doc.PersonID
WHERE doc.PersonID = @doctorID

DECLARE @id int
SET @id = 1
EXEC spGetDocPatients @doctorID = @id;

------------------------------------------------------------------------------
-- All the patients and doctors who had consultations on a given day. 
IF OBJECT_ID('spGetDayConsultations') IS NOT NULL
	DROP PROC spGetDayConsultations
GO 
CREATE PROC spGetDayConsultations
	@date date
AS 
SELECT visit.DateOfVisit
	, patient.FirstName + ' ' + patient.LastName [Patient's Name]
	, doc.FirstName + ' ' + doc.LastName [Doctor's Name]
FROM dbo.OfficeVisit [visit] INNER JOIN dbo.Person [doc]
ON visit.DoctorID = doc.PersonID
INNER JOIN dbo.Person [patient]
ON visit.PatientID = patient.PersonID
WHERE visit.DateOfVisit = @date; 

EXEC spGetDayConsultations @date = '01/02/2010'

------------------------------------------------------------------------------
/*
Create a stored procedure which I can execute to create a list of all patients who get prescribed medicine which is called asprin210
		- Patient name
		- Doctor name
		- Visit list
		- Doctor note
*/
IF OBJECT_ID('spGetPatientsPrescribedMedicine') IS NOT NULL
	DROP PROC spGetPatientsPrescribedMedicine;
GO 
CREATE PROC spGetPatientsPrescribedMedicine
	@medicine varchar(50)
AS
SELECT patient.FirstName + ' ' + patient.LastName [Patient's Name]
	, doc.FirstName + ' ' + doc.LastName [Doctor's Name]
	, visit.OfficeVisitID
	, visit.DoctorNote [Doctor's Note]
	, presType.PrescriptionName [Prescription]
FROM PatientPrescriptions [pat_pres] INNER JOIN Prescription [presType]
ON pat_pres.PrescriptionID = presType.PrescriptionID
INNER JOIN OfficeVisit [visit]
ON visit.OfficeVisitID = pat_pres.OfficeVisitID
INNER JOIN Person [patient]
ON patient.PersonID = visit.PatientID
INNER JOIN Person [doc]
ON doc.PersonID = visit.DoctorID
WHERE presType.PrescriptionName = @medicine;

EXEC spGetPatientsPrescribedMedicine @medicine = 'asprin210';

--------------------------------------------------------------------------------------
/*
Create a stored procedure which will accept a parameter which is patient name and give me a list of
	Patient name
	Prescription
*/

IF OBJECT_ID('spGetPatientPrescriptions') IS NOT NULL
	DROP PROC spGetPatientPrescriptions

GO 
CREATE PROC spGetPatientPrescriptions
	@firstName varchar(30),
	@lastName varchar(30)
AS
SELECT DISTINCT presType.PrescriptionName [Prescription]
	, patient.FirstName + ' ' + patient.LastName [Patient's Name]
FROM PatientPrescriptions [pat_pres] INNER JOIN Prescription [presType]
ON pat_pres.PrescriptionID = presType.PrescriptionID
INNER JOIN OfficeVisit [visit]
ON visit.OfficeVisitID = pat_pres.OfficeVisitID
INNER JOIN Person [patient]
ON patient.PersonID = visit.PatientID
WHERE patient.FirstName = @firstName AND patient.LastName = @lastName;

EXEC spGetPatientPrescriptions @firstName = 'James', @lastName = 'Hopkins'


---------------------------------------------------------------------

IF OBJECT_ID('spHasDoctorSeenPatientToday') IS NOT NULL
	DROP PROC spHasDoctorSeenPatientToday

GO
CREATE PROC spHasDoctorSeenPatientToday
	@timesSeenToday int = 0 OUTPUT,
	@doctorID int,
	@patientID int,
	@date date = NULL -- optional
AS 
IF @date IS NULL
	SET @date = GETDATE()
SELECT @timesSeenToday = COUNT(OfficeVisitID)
FROM dbo.OfficeVisit [visit]
WHERE DateOfVisit = @date AND visit.PatientID = @patientID AND visit.DoctorID = @doctorID

RETURN @timesSeenToday

DECLARE 
@visitDate date,
@docId int,
@patId int,
@result int

SET @visitDate = GETDATE()
SET @docID = 1
SET @patID = 2
SET @result = 0

EXEC spHasDoctorSeenPatientToday @timesSeenToday = @result OUTPUT, @date = @visitDate, @doctorID = @docID, @patientID = @patID
PRINT 'Times seen today: ' 
PRINT @result


---------------------------------------------------------------------------------------------------------
-- get all office visit with patient and doctor names
SELECT v.DateOfVisit, pat.PersonID, pat.FirstName, pat.LastName, v.DoctorID, doc.FirstName, doc.LastName
FROM dbo.OfficeVisit [v] INNER JOIN Person [pat]
ON v.PatientID = pat.PersonID
INNER JOIN Person [doc]
ON v.DoctorID = doc.PersonID
