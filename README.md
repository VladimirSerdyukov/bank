Данное приложение является результатом работы по заданию рекрутера.

Актуальная версия располагается в ветке rabbitBranch.
После скачивания на локальную машину, воспользуемся командой docker-compose up.
После актуализации контейнера приложение готово к работе:
внешний порт для взаимодействия :8080
points:
    GET /wallets/{id} - ожидает UUID;
    POST /wallet - ожидает тип данных приведенный ниже.

{
"walletId": UUID,
"operationType": DEPOSIT || WITHDRAW,
"amount": int (в приложении как Long но для запроса нет разницы)
}

Очереди созданные в RabbitMQ
    Две для внешнего подключения:
DirectExchange - "exchange-get", очередь - "queueRequestGet", routing-key - "request.get"
DirectExchange - "exchange-update", очередь - "queueRequestUpdate", routing-key - "request.update"
    Две для внутреннего использования
очередь - "queueResponseGet"
очередь - "queueResponseUpdate"