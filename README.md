# ETLine
**Проект, созданный в рамках Data Hack 2022, case #3.** 

## Структура JSON конфига
```json lines
{ // Config
  tasks: [
    {
      
      saveMode: "savemode", // Overwrite or Append
    }
  ],
  connections: [
    {
      connectionId: "string",
      type: "string",
      driver: "string", // postgresql or other
      host: "string", // localhost:5432
      dbName: "string", // name of database 
      user: "string",
      password: "string"
    }
  ]
}
```