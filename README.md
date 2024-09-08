# JavaConcurrencyPractice #

## since Java 1.0 until Java 1.5 ##
Жизненный цикл потока в вашей программе https://www.uml-diagrams.org/java-thread-uml-state-machine-diagram-example.html
Подробнее о потоках https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html

* __start__ - пример создания потоков и демонстрация его жизненного цикла:
  - ThreadCreation - примеры создания потоков;
  - liveness/LifeCycle - демонстрация состояний потоков;
  - liveness/InterruptedCycle - прерывание исполняемого метода потока.

* __sumofmany__ - совместная работа, на примере арифметических операций:
  - Summation - сравнения выполнения работы на одном и множестве потоков
  - __incordec__ - увеличение и уменьшение разделяемой переменной:
    - АsyncIncrementAndDecrement - без контроля доступа, см. различия на количестве операций;
    - IncrementAndDecrement - различные режимы доступа Mode.ASYNC, Mode.SYNC_OBJECT, Mode.SYNC_METHOD;
    - IncrementAndDecrementWithTerrorist - ошибка частичного ограничения доступа;
  - __deadlock__ - пример взаимоблокировки потоков на общих ресурсах при синхронизации мониторами
 
## since Java 5 java.util.concurrent ##
Больше информации о классах пакета java.util.concurrent по ссылке https://www.uml-diagrams.org/java-7-concurrent-uml-class-diagram-example.html

* __concurent__ - пример механизмов, используемых для синхронизации и обмена данными
  - __atomic__ - использование переменных с атомарными операциями изменения;
  - __barriers__ - примеры синхронизации работы потоков: 
     - BarrierSummation - через примитив синхронизации - барьер;
     - PhaserSummation - более гибкий пример поэтапной работы;
  - __messages__ - обмен данными между потоками:
    - CallbackMailbox - асинхронно через паттерн обратного вызова;
    - ExchangerReceiver - через синхронизируемый обменник;
  - __locks__ - создание критических секций кода:
    - MutexShuffling - уникальный доступ, ограниченный через примитив синхронизации - мьютекс;
    - ReadAndWriteShuffling - ограниченный доступ для разных групп;
    - SemaphoreShuffling - лимитированный доступ, ограниченный через примитив синхронизации - семафор;
    
* __executors__ - примеры исполнителей, скрывающих запуск потоков:
  - ExecutorsExample - запуск процедур и функций на пуле потоков
  - CompletableFutureExample - синхронный/асинхронный запуск задач (в фоновом потоке)

## Virtual Threads (jdk 21)
Wait for updates later