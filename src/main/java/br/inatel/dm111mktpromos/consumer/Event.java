package br.inatel.dm111mktpromos.consumer;

public record Event(EventType type, Operation operation, SuperMarketListMessage data) {
}
