package code.model
import org.scalatest.FunSuite
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import scala.collection.mutable.Stack
import net.liftweb.common.{Logger}
import net.liftweb.mapper.DB

class BookSuite extends FunSuite with Logger{
  
  // create book and two author instances and link each other to the book
  def createFixture = {
    new bootstrap.liftweb.Boot().boot
    // create the a book
    val book = Book.create.title("My Book").saveMe
    debug("created book with id: " + book.id.is)
    
    // create an author and link the book to the author
    val fred =  Author.create.firstName("Fred").saveMe
    debug("created author fred with id: " + fred.id.is)
    fred.books += book
    assert(fred.books.length == 1)
    assert(fred.books.contains(book))
   
    // save the link to the book to the database
    assert(fred.save)
    assert(DB.runQuery("select * from BookAuthors where book = " + book.id.is + " and author = " + fred.id.is )._2.length == 1)
    debug("fred.books now: " + fred.books )
    
    book.id.is
  }
 
  test("delete a object that is mapped via a many-to-many relationship to other objects") {
    // get the id of a book already created and link that to a new author
    val bookId = createFixture 
    val book = Book.find(bookId).open_!
   
    debug("deleting book from all author's books collection ...")
    book.authors.clear
    assert( book.save )
    assert(DB.runQuery("select * from BookAuthors where book = " + book.id.is )._2.isEmpty)
    
    // now delete the book itself from the database ...
    debug("delete the from the db ...")
    assert(book.delete_!)
    assert( DB.runQuery("select * from books where id = " + book.id.is)._2.isEmpty)
  
   
  }
  
}
