# TRMS
Tuition Reimbursement Management System

## Project Description

The purpose of TRMS is to provide a system that encourages quality knowledge growth relevant to an individual’s expertise.   Currently, TRMS provides reimbursements for university courses, seminars, certification preparation classes, certifications, and technical training.  The current system relies solely on email communication, requiring manual lookups of available funds and is error-prone due to inbox clutter and incorrect routing of tasks.  Furthermore, there is no way to record and report on reimbursements awarded, and so the company has no way to identify highly-invested courses that could be developed to be offered in-house.

## Technologies Used

* Java - SE1.8
* AWS Java SDK - 1.11.1004
* Apache Log4J - 2.11.2
* Javalin - 3.13.4
* Jackson - 2.10.3
* DataStax - 4.11.0

## Features

List of features ready and TODOs for future development
* The TRMS, on server start-up, automatically approves any requests that have been submitted without a timely response from the user's supervisor and dept. head.
* Benefits coordinators may choose to award employees a reimbursement amout other than the default amount based on available funding, event type, and event cost.
* Users can upload additional documents of any format; only the user and other interested parties (e.g. the employee's direct supervisors) will have access to these documents.

To-do list:
* Implement threading for the automated approval feature for timed out requests, so it checks on a schedule rather than just server start-up.
* A system to generate emails for additional information requests, as well as notification of reimbursement approvals, rejections, and award dispersals.
* Deploy server remotely using AWS EC2 services

## Getting Started
   
* git clone https://github.com/sctallman/TRMS
* Set up an AWS IAM user with programmatic access and AmazonKeyspacesFullAccess policy attached. Then generate security credentials for Amazon Keyspaces (for Apache Cassandra). SAVE THESE CREDENTIALS.
* The `application.conf` file (in src/main/resources) should be edited to provide your AWS region (line 3), AWS datacenter (line 10), and your truststore password of choice (line 16).
* Environment variables should be set up for the Driver: variable: `AWS_USER` value: `YOUR_KEYSPACES_USER`, variable: `AWS_PASS` value: `YOUR_KEYSPACES_PASS`
* Generate a truststore certificate: 
  - Using a command line terminal, navigate to the src/main/resources folder and issue the following commands:
```
curl https://certs.secureserver.net/repository/sf-class2-root.crt -O 
openssl x509 -outform der -in sf-class2-root.crt -out temp_file.der 
keytool -import -alias cassandra -keystore cassandra_truststore.jks -file temp_file.der 
```
* - When prompted for a password, use the one you chose in line 16 of the application.conf file.
* Set up an AWS IAM user with programmatic access and Amazons3FullAccess policy attached. Create a bucket in S3 with this user.
* Edit src/main/java/com/sct/utils/S3Util.java
  - At line 15 you should replace `Region.US_EAST_2` with your AWS region
  - At line 16 you should replace `reactive-2103-0329` with your S3 bucket name

## Usage

* After setting things up, run Driver.java to start the server. On first run, TRMS will generate a keyspaces and related tables. 
* After confirming generation of these tables in your AWS keyspaces console, restart the server.
* The included Postman collection offers a starting point for how to build HTTP requests for this project.
