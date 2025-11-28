# Crypto Recommendation Service

### System description
The Crypto Investment Recommendation Service is designed to help developers make informed decisions about investing in cryptocurrencies. 
By analyzing historical price data from CSV files for various cryptos, the service provides actionable insights and recommendations through a simple API.

#### Inside
1) [Test task](Crypto Recommendations Service.pdf)
2) [OpenApi specification](src/main/resources/openapi/crypto-spec.yaml)
3) [Postman collection](Crypto.postman_collection.json)

#### What was implemented
1) 3 Api based on test task
2) To all 3 api was added date range
3) API generation with gradle based on specification
4) Once on app start initial load data into H2 DB
5) Possible run in container
6) IP filtering from DDOS on basic level

#### Questions from task
Q: Initially the cryptos are only five, but what if we want to include more? Will the
recommendation service be able to scale? 

A: Just update CryptoType enum and contract. Add new one

Q: New cryptos pop up every day, so we might need to safeguard recommendations service
endpoints from not currently supported cryptos

A: There is validation on API level which will fail request with incorrect crypto. Also values stored in enum

Q: For some cryptos it might be safe to invest, by just checking only one month's time
frame. However, for some of them it might be more accurate to check six months or even
a year. Will the recommendation service be able to handle this?

A: For all APIs was added 'from' and 'to' optional properties. If they will be resent user can get 'recommendation' for any period of time

#### Concerns and what can be improved
1) It task everything was related to one day. For flexibility is better to add time also
2) IP filtering is working on base level. For real world solution filtering better to be on gateway level and it is good to add in header x-forwarded-for property to work with different 'proxies'. 
Also 10 request per minute is now hardcoded in filter class, better to move to app.properties
3) Initial load: For the real world scenario need to know context how much and how often new values will appear
    * Initial load should be one time operation. Possibly with just insert into DB (easiest)
   or some dataflow for big numbers of data
    * It is dynamic unlimited during the day - some messaging/listener should be introduced
    * If it is once per day file with values are loaded - then some job.
   Which also can calculate most common API call calculation.
   For example - precalculate and store in db normalized range for all crypto. Or some basic statistic
4) Now all data aggregation for response is on DB query level - it can cause performance issues potentially.

### How to run
1) Build project gradlew clean build
> gradlew clean build

this will build project and generate controller interface based on crypto-spec.yaml specification
If RecommendationController will not see it - refresh gradle project
2) Run application locally 
> gradlew bootRun

Or
setup container
> docker compose up

3) Use application with provided postman collection

