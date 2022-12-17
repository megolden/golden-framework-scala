package golden.framework.web

import java.io.InputStream

trait UploadedFile:
  def content: InputStream
  def filename: String
  def size: Long
  def contentType: String
