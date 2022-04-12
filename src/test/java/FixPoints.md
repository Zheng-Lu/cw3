AddEventPerformanceSystemTests >
- java.lang.NullPointerException
	at logging.LogEntry.<init>(LogEntry.java:13)
	at logging.Logger.logAction(Logger.java:27)
  at command.LogoutCommand.execute(LogoutCommand.java:33)

BookEventSystemTests >
- 'BookEventCommand(long, long, int)' is not public in 'command.BookEventCommand'. Cannot be accessed from outside package
- getResult() in ListEventsCommand.java:
  Required type: List<model.Event>
  Provided: Object
  
- java.lang.NullPointerException
	at logging.LogEntry.<init>(LogEntry.java:13)
	at logging.Logger.logAction(Logger.java:27)
	at command.CreateTicketedEventCommand.execute(CreateTicketedEventCommand.java:64)
  
CancelEventSystemTests >
- 'CancelEventCommand(long, java.lang.String)' is not public in 'command.CancelEventCommand'. Cannot be accessed from outside package
- getResult() in ListEventsCommand.java:
  Required type: List<model.Event>
  Provided: Object

RespondSponsorshipSystemTests >
- getResult() in ListSponsorshipRequestsCommand.java:
  Required type: List<model.SponsorshipRequest>
  Provided: Object

- java.lang.NullPointerException
	at logging.LogEntry.<init>(LogEntry.java:13)
	at logging.Logger.logAction(Logger.java:27)
  at command.LogoutCommand.execute(LogoutCommand.java:33)
