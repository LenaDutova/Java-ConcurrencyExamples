# JavaConcurrencyPractice #

## since Java 1.0 until Java 1.5 ##

* __ThreadCreation__ - примеры создания потоков;
* __sumofmany__ - пример сравнения выполнения работы на одном и множестве потоков;
* __incordec__ - увеличение и уменьшение разделяемой переменной:
  - АsyncIncrementAndDecrement - без контроля доступа, см. различия на количестве операций;
  - IncrementAndDecrement - различные режимы доступа Mode.ASYNC, Mode.SYNC_OBJECT, Mode.SYNC_METHOD;
  - IncrementAndDecrementWithTerrorist - ошибка частичного ограничения доступа;
* __liveness__ - демонстрация жизненного цикла потоков:
  - LifeCycle - демонстрация состояний потоков;
  - InterruptedCycle - прерывание исполняемого метода потока.
    
   
## since Java 5 java.util.concurrent ##
Больше информации о классах пакета java.util.concurrent по ссылке https://www.uml-diagrams.org/java-7-concurrent-uml-class-diagram-example.html
* __atomic__ - использование переменных с атомарными операциями изменения;
* __barriers__ - примеры синхронизации работы потоков: 
   - BarrierSummation - через примитив синхронизации - барьер;
   - PhaserSummation - более гибкий пример поэтапной работы;
* __messages__ - обмен данными между потоками:
  - CallbackMailbox - асинхронно через паттерн обратного вызова;
  - ExchangerReceiver - через синхронизируемый обменник;
* __locks__ - создание критических секций кода:
  - MutexShuffling - уникальный доступ, ограниченный через примитив синхронизации - мьютекс;
  - ReadAndWriteShuffling - ограниченный доступ для разных групп;
  - SemaphoreShuffling - лимитированный доступ, ограниченный через примитив синхронизации - семафор;
* __executors__ - примеры исполнителей, скрывающих запуск потоков:
  - Executor



