
## Entrega 6 - Elastic Search
El CEO ahora nos pide que exista una macroeconomía (Trade Broker) en el universo de Epers Tactics. Por lo cual se pide que implementen un buscador de items compra/venta, a la larga va a querer saber cuales fueron las cosas más comprados/vendidos y tener un rastreo de ellos (kibana).

## Servicios

Se deberá implementar un nuevo servicio `MarketService` que implemente los siguientes métodos:

- `todasLasFormaciones():List<Formacion>`: Devuelve todas las formaciones creadas.