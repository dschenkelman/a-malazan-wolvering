
namespace PhoneTicket.Web.Tests.Data
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    [TestClass]
    public class RepositoryFixture
    {
        private Mock<IContext> context;

        private MockRepository mockRepository;

        private Mock<MockableDbSet<Movie>> movies;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Strict);
            this.context = this.mockRepository.Create<IContext>();
            this.movies = this.mockRepository.Create<MockableDbSet<Movie>>();

            this.context.Setup(c => c.Set<Movie>()).Returns(this.movies.Object);
        }

        [TestMethod]
        public async Task ShouldInvokeContextSaveChangesAsyncWhenSaveAsyncIsInvoked()
        {
            // arrange
            this.context.Setup(c => c.SaveChangesAsync()).Returns(Task.FromResult(0)).Verifiable();

            var repository = this.CreateRepository();

            // act
            await repository.SaveAsync();

            // assert
            this.context.Verify(c => c.SaveChangesAsync(), Times.Once());
        }

        [TestMethod]
        public async Task ShouldFindMovieByIdWhenRetrievingMovieById()
        {
            // arrange
            const int KeyToFind = 3;
            const string MovieTitle = "Lord of the rings: The fellowship of the ring";

            Movie movie = new Movie { Id = KeyToFind, Title = MovieTitle };

            this.movies.Setup(s => s.FindAsync(KeyToFind)).Returns(Task.FromResult(movie)).Verifiable();

            var repository = this.CreateRepository();

            // act
            var retrievedMovie = await repository.GetByKeyValuesAsync(KeyToFind);

            // assert
            Assert.AreSame(retrievedMovie, movie);
            Assert.AreEqual(MovieTitle, retrievedMovie.Title);
            this.movies.Verify(s => s.FindAsync(KeyToFind), Times.Once());
        }

        [TestMethod]
        public void ShouldReturnMoviesThatMatchFilterWhenRetrievingMovies()
        {
            // arrange
            Movie movie1 = new Movie { Id = 1 };
            Movie movie2 = new Movie { Id = 2 };
            Movie movie3 = new Movie { Id = 3 };

            var moviesToReturn = new List<Movie> { movie1, movie2, movie3 };

            var queryableMovies = moviesToReturn.AsQueryable();

            this.movies.Setup(s => s.Expression).Returns(queryableMovies.Expression);
            this.movies.Setup(s => s.Provider).Returns(new TestDbAsyncQueryProvider<Movie>(queryableMovies.Provider)).Verifiable();

            var repository = this.CreateRepository();

            // act
            var retrievedMovies = repository.Filter(s => s.Id % 2 != 0).ToList();

            // assert
            Assert.AreEqual(2, retrievedMovies.Count);
            Assert.AreSame(movie1, retrievedMovies[0]);
            Assert.AreSame(movie3, retrievedMovies[1]);
        }

        [TestMethod]
        public void ShouldCallAddOnSetWhenInsertingMovie()
        {
            // arrange
            var movie1 = new Movie { Id = 1 };
            var movie2 = new Movie { Id = 2 };

            var repository = this.CreateRepository();

            this.movies.Setup(s => s.Add(movie1)).Returns(movie1).Verifiable();
            this.movies.Setup(s => s.Add(movie2)).Returns(movie2).Verifiable();

            // act
            repository.Insert(movie1);
            repository.Insert(movie2);

            // assert);
            this.movies.Verify(s => s.Add(movie1), Times.Once());
            this.movies.Verify(s => s.Add(movie2), Times.Once());
        }

        [TestMethod]
        public async Task ShouldFindSubjectByIdAndThenRemoveWhenDeletingSubjectById()
        {
            // arrange
            const int IdToDelete = 1;
            Movie movie = new Movie { Id = IdToDelete };

            var repository = this.CreateRepository();

            this.movies.Setup(s => s.FindAsync(IdToDelete)).Returns(Task.FromResult(movie)).Verifiable();
            this.movies.Setup(s => s.Remove(movie)).Returns(movie).Verifiable();

            // act
            await repository.DeleteAsync(IdToDelete);

            // assert;
            this.movies.Verify(s => s.FindAsync(IdToDelete), Times.Once());
            this.movies.Verify(s => s.Remove(movie), Times.Once());
        }

        private Repository<Movie> CreateRepository()
        {
            return new Repository<Movie>(this.context.Object);
        }

        public abstract class MockableDbSet<T> : DbSet<T>, IQueryable<T>
            where T : class
        {
            public abstract IEnumerator<T> GetEnumerator();

            public abstract Expression Expression { get; }

            public abstract Type ElementType { get; }

            public abstract IQueryProvider Provider { get; }
        }

        public class TestDbAsyncQueryProvider<TEntity> : IDbAsyncQueryProvider
        {
            private readonly IQueryProvider _inner;

            internal TestDbAsyncQueryProvider(IQueryProvider inner)
            {
                _inner = inner;
            }

            public IQueryable CreateQuery(Expression expression)
            {
                return new TestDbAsyncEnumerable<TEntity>(expression);
            }

            public IQueryable<TElement> CreateQuery<TElement>(Expression expression)
            {
                return new TestDbAsyncEnumerable<TElement>(expression);
            }

            public object Execute(Expression expression)
            {
                return _inner.Execute(expression);
            }

            public TResult Execute<TResult>(Expression expression)
            {
                return _inner.Execute<TResult>(expression);
            }

            public Task<object> ExecuteAsync(Expression expression, CancellationToken cancellationToken)
            {
                return Task.FromResult(Execute(expression));
            }

            public Task<TResult> ExecuteAsync<TResult>(Expression expression, CancellationToken cancellationToken)
            {
                return Task.FromResult(Execute<TResult>(expression));
            }

            private class TestDbAsyncEnumerable<T> : EnumerableQuery<T>, IDbAsyncEnumerable<T>
            {
                public TestDbAsyncEnumerable(IEnumerable<T> enumerable)
                    : base(enumerable)
                {
                }

                public TestDbAsyncEnumerable(Expression expression)
                    : base(expression)
                {
                }

                public IDbAsyncEnumerator<T> GetAsyncEnumerator()
                {
                    return new TestDbAsyncEnumerator<T>(this.AsEnumerable().GetEnumerator());
                }

                IDbAsyncEnumerator IDbAsyncEnumerable.GetAsyncEnumerator()
                {
                    return GetAsyncEnumerator();
                }

                public IQueryProvider Provider
                {
                    get
                    {
                        return new TestDbAsyncQueryProvider<T>(this);
                    }
                }
            }

            private class TestDbAsyncEnumerator<T> : IDbAsyncEnumerator<T>
            {
                private readonly IEnumerator<T> _inner;

                public TestDbAsyncEnumerator(IEnumerator<T> inner)
                {
                    _inner = inner;
                }

                public void Dispose()
                {
                    _inner.Dispose();
                }

                public Task<bool> MoveNextAsync(CancellationToken cancellationToken)
                {
                    return Task.FromResult(_inner.MoveNext());
                }

                public T Current
                {
                    get
                    {
                        return _inner.Current;
                    }
                }

                object IDbAsyncEnumerator.Current
                {
                    get
                    {
                        return Current;
                    }
                }
            }
        }
    }
}