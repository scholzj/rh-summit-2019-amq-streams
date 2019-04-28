# Red Hat Summit 2019 - AMQ Streams demo

This demo shows deployment of AMQ Streams 1.1.0 on OpenShift together with a simple demo application which demonstrates different common patterns used with Apache Kafka.

## Create Projects / namespaces

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

## Deploy the price feed

The price feed will send updates with new prices of selected shares.
The price feed here is just a demo.
It generates random prices for selected stock tickers.
It runs as a plugin inside Kafka Connect to demonstrate Kafka Connect and possibility to write your own connectors.
The share prices are sent as individual events with the share code as key and the price as the payload.

* Deploy Kafka Connect and a simple UI for monitoring the prices

```
oc apply -f 04-price-feed.yaml
```

* Add the Price feed connector to the image

```
oc start-build my-connect-cluster-connect --from-dir ./05-price-feed-connector
```

* Check that the connector plugin was build into the image (uses [HTTPie](https://httpie.org/))

```
http http://my-connect-cluster-amq-streams-demo.127.0.0.1.nip.io/connector-plugins
```

* Deploy the price feed connector (uses [HTTPie](https://httpie.org/))

```
http http://my-connect-cluster-amq-streams-demo.127.0.0.1.nip.io/connectors < 06-price-feed-connector.json
```

* Check that the pods are working and use to UI to see how the prices are generated.

## Deploy the trade aggregator

Having the trade events is good.
You can work with the event stream and analyze the trades.
But sometimes, you do not care about the trades and you just care about how many shares do you have.
So we need to aggregate the trades into a portfolio which would show how many shreas of different companies are in the portfolio.
This is done using a simple Kafka Streams application which reads the stream of trades and aggregates them into the portfolio.
It will sum all trades for given share code and send them as a message to another topic.
The topic with the portfolios is compacted, because we care only about the latest values.

* Deploy the price feed generator and a simple UI for monitoring the prices
```
oc apply -f 07-portfolio.yaml
```

* Check that the pods are working and use to UI to check your portfolio. Try to enter new trades in the UI from the previous step to see how the portfolio is updated.

## Deploy the pricer

Once we have the portfolio, we can use the latest prices we receive from the price feed and evaluate the portfolio.
When we join the latest share price by the latest number of shares we own, we get the value of that portfolio.
Again, this is implemented by a simple Kafka Streams application which reads data from both topics, joins them by calculating the value of the shares we own and sends the result to another Kafka topic. 

* Deploy the pricer and a simple UI for monitoring the prices
```
oc apply -f 08-pricer.yaml
```

* Check that the pods are working and use to pricer UI to see the priced portfolio
