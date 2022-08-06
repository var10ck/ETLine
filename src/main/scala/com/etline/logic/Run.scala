package com.etline.logic

import com.etline.engine.IO.{Loader, Reader}
import com.etline.utils.ContextImplicits._

object Run extends App {
  val tasks = config.tasks

  for{
    task <- tasks
    table <- Reader.readAll(task)
  } yield Loader.load(table, task)

}
