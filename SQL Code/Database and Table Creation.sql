use DoctorOffice;

create database DoctorOffice

use DoctorOffice

create table Person (
	PersonID int PRIMARY KEY,
	FirstName nvarchar(25) NOT NULL,
	MiddleName nvarchar(25),
	LastName nvarchar(25) NOT NULL,
	StreetAddress nvarchar(50) NOT NULL,
	City nvarchar(25) NOT NULL,
	State nvarchar(25) NOT NULL,
	ZIP char(5) NOT NULL,
	DateOfBirth date NOT NULL,
	PrimaryPhone nvarchar(25) NOT NULL,
	SecondaryPhone nvarchar(25)
)

create table OfficeVisit 
(
	OfficeVisitID int PRIMARY KEY,
	PatientID int references Person(PersonID) NOT NULL,
	DoctorID int references Person(PersonID) NOT NULL,
	DateOfVisit date NOT NULL,
	DoctorNote nvarchar(300),
	CONSTRAINT chID CHECK (PatientID <> DoctorID)
)

create table Specialty
(
	SpecialtyID int PRIMARY KEY,
	SpecialtyName nvarchar(25) NOT NULL
)


create table DoctorSpecialties
(
	DocSpecID int PRIMARY KEY,
	DoctorID int references Person(PersonID),
	SpecialtyID int references Specialty(SpecialtyID)
)

create table Prescription 
(
	PrescriptionID int PRIMARY KEY,
	PrescriptionName nvarchar(25) NOT NULL
)

create table PatientPrescriptions
(
	PatientPresciptionID int PRIMARY KEY,
	PatientID int references Person(PersonID) NOT NULL,
	OfficeVisitID int references OfficeVisit(OfficeVisitID) NOT NULL,
	PrescriptionID int references Prescription(PrescriptionID) NOT NULL
)

create table Test 
(
	TestID int PRIMARY KEY,
	TestName nvarchar(25) NOT NULL
)

create table OfficeVisitTests
(
	OfficeVisitTestID int PRIMARY KEY,
	OfficeVisitID int references OfficeVisit(OfficeVisitID) NOT NULL,
	TestID int references Test(TestID) NOT NULL
)

create table Audit 
(
	AuditID int PRIMARY KEY,
	PatientName nvarchar(30),
	TheAction nvarchar(10),
	Prescription nvarchar(30),
	DateOfModification date
)


