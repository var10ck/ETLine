# ETLine

**Проект, созданный в рамках Data Hack 2022, case #3.**

## Структура JSON конфигов

```json5
{
  // Config
  "tasks": [
    {
      "saveMode": "savemode", // Overwrite or Append
      "dbSource": {
        "connectionId": "string",
        "tables": [
          {
            "name": "string",
            "hwmColumnName": "string",
            "targetName": "string",
            "readOptions": {
              "key": "value"
            },
            "batchLoad": {
              "byColumn": "string",
              "partitionBy": "string", //date
              "interval": "string" // 1 month, 1 year 
            }
          },
        ]
      },
      //or
      "fileSource": {
        "connectionId": "string",
        "files": [
          {
            "path": "string",
            "hwmColumnName": "string",
            "targetName": "string",
            "readOptions": {
              "key": "value"
            },
            "batchLoad": {
              "byColumn": "string",
              "partitionBy": "string", //date
              "interval": "string" // 1 month, 1 year 
            }
          }
        ],
      },
      "target": {
        "connectionId": "string",
        "format": "string",
        "path": "string",
        "writeOptions": {
          "key": "value"
        }
      },
      "sparkSessionConf": {
        "key": "value"
      }
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
      "driver": "string",
      // postgresql or other
      "host": "string",
      // localhost:5432
      "dbName": "string",
      // name of database 
      "user": "string",
      "password": "string"
    }
  ],
  "hdfsConnections": [
    {
      "connectionId": "string",
      "url": "string"
    }
  ]
}

```