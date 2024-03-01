### Introduction
This project works as a personal information storage library.
Users can record create online jounals to store their insights state-of-art techniques in software development.

### Technology Choice
#### Frontend development technology list: 

  **Language**: TypeScript
  
  **Framework**: Vue3

  **Build Tool**: Vue Cli 
  
  **Dependency Manager**:npm
  
  **Visual UI Components**: Ant Design Vue, Escharts, wangeditor

  
#### Backend development technology list:

  **Language**: Java
  
  **Framework**: Spring Framework
  
  (**Controller Layer**: SpringMVC , **DAO Layer**: Mybatis)
  
  **Build Tool**: Spring Boot
  
  **DBMS**: MySQL
  
  **Dependency Manager**: Maven
  
  **In-Memory Cache**:Redis


### Features
#### Viewer Management:
  **Viewer Login:** Viewers can login based on their username and password. Password are double-encryted via MD5 algorithm in both frontend and backend. Viewers can logout manually or automatically when the session ends.
  
  **Viewer Administration:** Viewers can Add, Update, View, Remove their details and Reset password.  


#### Ebook Management:
  **Category Manipulation:** Authorised viewers can create, delete, update, view category details. Normal viewer can select categories and view details of categories. In this project, it supports first level and secondary level category like Java/Spring framework.
  
  **Ebook Manipulation:** Authorised Viewer can create, delete, update, view ebook details. Normal Viewer can read Ebook based on their selected category. 
                  
#### Document Management:
  **Document Manipulation:** Authorised viewers can create, delete, update, view documents written in a ebook. Normal viewer can select ebook and read its document. The tree depth of document is unlimited, for example, it can be 1.1 or 1.1.1.1.1.1....
  
  **Vote document:** User can vote on a document. All other Viewer will be notified by this action.

#### Statistics:
  **Statistics Display:** Authorised viewers can view details of statistics like today/total view count and vote count increment on welcome webpage.
  
  **Statistics Snapshot:** Backend will update statistics for all day's total view count and vote count every 12 hours, and today's view count and vote count every 10 mins.


### Run Project on Local Machine:
**Frontend:**
  1. npm --install
  2. npm run serve-dev
     
**Backend:**
The recommanded IDE for development is IDEA.
  1. Setup database settings at applicaiton.properties and make sure IDEA can connect to remote/local MYSQL database and Redis server.
  2. Execute all.sql.
  3. maven compile. This will download all maven dependency.
  4. Compile and run WikiApplication.java.

### Deployment:
  **Instance**: Aliyun ESC server.
  **Database**: Aliyun RDS MySQL database.
  **Cache**: Aliyun Redis Cache. (To be removed due to its cost.)

### Test Account
Username: test
Password: test

### Special Thanks
Many thanks to Jia, Wa at Mooc Teaching Platform for instruction.
