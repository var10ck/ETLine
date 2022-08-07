package com.etline.ods

import java.util.UUID

class Ids {
  // TODO: temporary solution
  def getId: String = UUID.randomUUID().toString
}

class ExtractId extends Ids

class ClosedExtractId extends Ids
