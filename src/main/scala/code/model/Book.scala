package code.model 

import net.liftweb._
import mapper._
import util._
import common._

class Book extends LongKeyedMapper[Book]
                                   with CreatedUpdated with IdPK
                                   with ManyToMany { 
	def getSingleton = Book
	object title extends MappedString(this, 255)
	object authors extends MappedManyToMany( 
			BookAuthors, BookAuthors.book, BookAuthors.author, Author) 
}

object Book extends Book with LongKeyedMetaMapper[Book]{ 
	override def dbTableName = "books"
}

class Author extends LongKeyedMapper[Author]
                                     with CreatedUpdated with IdPK
                                     with ManyToMany { 
	def getSingleton = Author
	object firstName extends MappedString(this, 255)
	object books extends MappedManyToMany( BookAuthors, BookAuthors.author, BookAuthors.book, Book)
}

object Author extends Author with LongKeyedMetaMapper[Author]{ 
	override def dbTableName = "authors"
}

object BookAuthors extends BookAuthors with MetaMapper[BookAuthors]

class BookAuthors extends Mapper[BookAuthors] {
	def getSingleton = BookAuthors
	object author extends LongMappedMapper(this, Author) 
	object book extends LongMappedMapper(this, Book) 
}
