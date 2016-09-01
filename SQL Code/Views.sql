USE DoctorOffice

/*
  Create a view(Encrypted) vDocPatient , that gives me a list of the following
            Patient full name (patient first name patient last name (In one column))
            Doctor full name
            Visit date
*/

-- shows all visits - includes repeat patients
drop view vwDocPatient;

GO
CREATE VIEW vwDocPatient 
WITH ENCRYPTION, SCHEMABINDING 
AS 
SELECT pat.FirstName + ' ' + pat.LastName [Patient's Name]
	, doc.FirstName + ' ' + doc.LastName [Doctor's Name]
	, DateOfVisit [Visit Date]
FROM dbo.OfficeVisit [v] INNER JOIN dbo.Person [doc]
ON v.DoctorID = doc.PersonID
INNER JOIN dbo.Person [pat]
ON v.PatientID = pat.PersonID;

select * from vwDocPatient;

 
 /*
    Create view vPatientPrescrip that gives me a list of
            Patient full name (patient first name patient last name (In one column))
            If they were given any tests then the test name.
*/

drop view vwPatientPrescrip; -- get rid of

GO
CREATE VIEW vwPatientPrescrip
WITH SCHEMABINDING 
AS 
SELECT patient.FirstName + ' ' + patient.LastName [Patient's Name]
	, testType.TestName
	-- visit.OfficeVisitID -- get rid of later
	-- , vTest.TestID -- can get rid of
FROM dbo.OfficeVisit [visit] LEFT OUTER JOIN dbo.OfficeVisitTests [vTest]
ON visit.OfficeVisitID = vTest.OfficeVisitID
INNER JOIN dbo.Person [patient] 
ON visit.PatientID = patient.PersonID
LEFT OUTER JOIN dbo.Test [testType]
ON vTest.TestID = testType.TestID;


select * from vwPatientPrescrip;
