# ETLine

**Проект, созданный в рамках Data Hack 2022, case #3.**

## Структура JSON конфигов

```json5
{
  // Config
  tasks: [
    {
      type: "string",
      saveMode: "savemode", // Overwrite or Append
      source: {
        connection_id: "string",
        tables: [
          {
            name: "string",
            columns: ["string"], // array of column names or ["*"]
            
          }
        ]
      },
      sparkSessionConf: ["string"]
    }
  ],
}
```

Информация о системах источников хранится в отдельном файле

```json5
{
  connections: [
    {
      connectionId: "string",
//      type: "string",
      driver: "string", // postgresql or other
      host: "string", // localhost:5432
      dbName: "string", // name of database 
      user: "string",
      password: "string"
    }
  ]
}
```