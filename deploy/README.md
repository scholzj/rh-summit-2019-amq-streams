# Red Hat Summit 2019 - AMQ Streams demo

This demo shows deployment of AMQ Streams 1.1 on OpenShift together with a simple demo application which demonstrates different common patterns used with Apache Kafka.

## Create Projects / namespaces

* Create new projects for the demo
```
oc new-project amq-streams-demo
```

## Install AMQ Streams operator

* Install AMQ Streams 1.1
```
oc apply -f 01-install/
```

## Deploy Kafka

* Install Kafka cluster
```
oc apply -f 02-kafka.yaml
```

## Deploy the trade manager

The trade manager will let you enter trades.

* Deploy the price feed generator and a simple UI for monitoring the prices
```
oc apply -f 03-trade-manager.yaml
```

* Check that the pods are working and use to UI to enter new trades. You can use negative amounts to sell shares.

## Deploy the price feed

The price feed here is just a demo. It generates random prices for selected stock tickers. It runs as a plugin inside Kafka Connect.

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

The portfolio aggregator will take the trades you created and aggregate them into a portfolio. 
A portfolio will give you a clear view of what you own.

* Deploy the price feed generator and a simple UI for monitoring the prices
```
oc apply -f 07-portfolio.yaml
```

* Check that the pods are working and use to UI to check your portfolio. Try to enter new trades in the UI from the previous step to see how the portfolio is updated.

## Deploy the pricer

The pricer will price the portfolio. 
It will join the stream with prices with the stream with portfolios and tell you how much are the positions in your portfolio worth.

* Deploy the price feed generator and a simple UI for monitoring the prices
```
oc apply -f 08-pricer.yaml
```

* Check that the pods are working and use to pricer UI to see the priced portfolio
