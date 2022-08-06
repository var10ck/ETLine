# ETLine

**Проект, созданный в рамках Data Hack 2022, case #3.**

## Структура JSON конфигов

```json5
{
  // Config
  "tasks": [
    {
      // type: "string",
      "saveMode": "savemode", // Overwrite or Append
      "batchLoad": {
        "byColumn": "string",
        "partitionBy": "string", //date
        "interval": "string" //1 mounth, 1 year 
      },
      "source": {
        "connectionId": "string",
        "tables": [
          {
            "name": "string",
            "columns": ["string"], // array of column names or ["*"]	
            "hwmColumnName": "string"
          }
        ]
      },
      "target": {
        "connectionId": "string",
        "format": "string",
        "path": "string",
        "writeOptions": {"key": "value"}
      },
      "sparkSessionConf": {"key": "value"}
    }
  ]
}

```

Информация о системах источников хранится в отдельном файле

```json5
{
  "dbConnections": [
    {
      "connectionId": "string",
      "driver": "string", // postgresql or other
      "host": "string", // localhost:5432
      "dbName": "string", // name of database 
      "user": "string",
      "password": "string"
    }
  ],
  "hdfsConnections": [
    {
      "connection_id": "string",
      "url": "string"
    }
  ]
}

```