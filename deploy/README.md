# Red Hat Summit 2019 - AMQ Streams demo

This demo shows deployment of AMQ Streams 1.1.0 on OpenShift together with a simple demo application which demonstrates different common patterns used with Apache Kafka.

## Preparation

* Create new projects for the demo

```
oc new-project amq-streams-demo
```

## Install AMQ Streams operator

* Install AMQ Streams 1.1.0

```
oc apply -f 01-install/
```

## Deploy Kafka

* Install Kafka cluster

```
oc apply -f 02-kafka.yaml
```

* Wait until it is read

## Deploy the trade manager

The trade manager will let you enter trades.
The trades are entered as events -> each trade will be sent to Kafka as a single message and build a log with individual trades.

* Deploy the trade manager, which is a simple web based UI where you can enter the trades 

```
oc apply -f 03-trade-manager.yaml
```

* Check that the pods are working and use to UI to enter new trades. You can use negative amounts to indicate selling shares.

## Deploy Kafka Connect

Kafka Connect will be used to get the latest pricing information for our shares.
We will use a custom connector to get the prices (just generated in reality).
We will use here pre-build image which already contains the connector.
You can also use Source2Image to build a new image with the connector.
But that takes too much time.

* Deploy Kafka Connect

```
oc apply -f 04-kafka-connect.yaml
```

* Check that Kafka Connect was deployed and is working.

* Check that the connector plugin is there in the image (uses [HTTPie](https://httpie.org/))

```
http http://my-connect-cluster-amq-streams-demo.apps.jscholz.devcluster.openshift.com/connector-plugins/
```

## Deploy the price feed connector

The price feed will send updates with new prices of selected shares.
The price feed here is just a demo.
It generates random prices for selected stock tickers.
It runs as a plugin inside Kafka Connect to demonstrate Kafka Connect and possibility to write your own connectors.
The share prices are sent as individual events with the share code as key and the price as the payload.

* Update the Kafka Connect use and create the topic where it will send the prices.
This also deploys the UI for checking the price updates. 

```
oc apply -f 05-price-feed.yaml
```

* Deploy the price feed connector (uses [HTTPie](https://httpie.org/))

```
http http://my-connect-cluster-amq-streams-demo.apps.jscholz.devcluster.openshift.com/connectors < 06-price-feed-connector.json
```

* Check that the pods are working and use to UI to see how the prices are generated.

## Deploy the trade aggregator and pricer

Having the trade events is good.
You can work with the event stream and analyze the trades.
But sometimes, you do not care about the trades and you just care about how many shares do you have and what is their value.
So we need to aggregate the trades into a portfolio which would show how many shreas of different companies are in the portfolio.
And once we have the portfolio, we can use the latest prices we receive from the price feed and evaluate the portfolio.
When we join the latest share price by the latest number of shares we own, we get the value of that portfolio.
This is implemented by a simple Kafka Streams application which reads data from both topics, aggregates the positions and joins them by calculating the value of the shares we own and sends the result to another Kafka topic. 

* Deploy the pricer and a simple UI for monitoring the prices
```
oc apply -f 07-pricer.yaml
```

* Check that the pods are working and use to pricer UI to see the portfolio and its value
