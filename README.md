# Q1
```
Schreiben Sie ein Programm (myapp) in Sprachen Ihrer
Wahl, das einen UDP Socket aufmacht und auf Daten lauscht. Dieses
Programm soll eine bestimmte mathematische Funktion implemen-
tieren und die auf jede empfangene Byte durchführen. Die Ergebnis-
sen werden über einen anderen Socket an einen anderen Funktion
weitergeleitet.
Die Konfigurationsparameter für dieses Programm sind:
• Portnummer zum lauschen
• IP-Adresse & Port (im Form IP:Port) zum Senden
• Funktionen zum implementieren
– Addition (+1)
– Subtraktion (-1)
– Left Shift bzw. Verdopplung
– Right Shift bzw. Halbierung
Overflow und underflow Fälle können in der Funktionen ignoriert
werden. Die Parametern sollen für die Richtigkeit geprüft werden
bevor das Programm gestartet wird.
Testen Sie ihr Programm mit netcat (nc) und stellen Sie sicher,
dass es korrekt funktioniert. Verwenden Sie nc -u <ip> <port> um
UDP-Pakete zu senden und zu empfangen.
```
Diese Funktionalität wurde wie folgt umgesetzt:
Die Klasse `MainLauncher.java` nimmt die notwendigen Parameter über Environment Variablen:
- `SENDING_PORT` = Port, auf den die verarbeiteten Bytes gesendet werden
- `SENDING_ADDRESS` = Adresse, auf die verarbeiteten Bytes gesendet werden
- `LISTENING_PORT` = Port, auf dem auf kommende Bytes gelauscht wird
- `OPERATING_MODE` = Auswahl der Option, welche auf die Bytes ausgeführt wird:
  - 0 ist bitshift left (mult by 2)
  - 1 ist bitshift right (div by 2)
  - 2 ist Addition
  - 3 ist Subtraktion       

Alle eingelesenen Environment Variablen werden überprüft nach Richtigkeit; sofern diese gewährleistet wird, werden Sockets aufgemacht
Das Testen des Programms wurde mit Socat durchgeführt:     
Listening done via: `socat -v UDP-RECVFROM:9001,fork STDOUT`    
Sending done via: `echo "12850212" | socat - UDP4-SENDTO:localhost:9000`

# Q2
```
In diesem Schritt werden Sie einen Docker-Container er-
stellen, der das Programm myapp ausf¨uhrt. Erstellen Sie ein Dockerfile,
das die notwendigen Schritte zum Erstellen des Containers beschreibt.
Stellen Sie sicher, dass der Container die Konfigurationsparameter
als Umgebungsvariablen akzeptiert.
Bauen Sie das Docker-Image mit dem Namen myapp image. Erstel-
len Sie ein Docker Compose-File, der drei Containers (Namen C1, C2
und C3) aus dem Image startet. Alle Containers sollen auf dem Port
8080 lauschen. Container C1 soll die Addition durchf¨uhren, C2 die
Verdopplung und C3 die Subtraktion. Container C1 soll die Ergeb-
nisse an C2 weiterleiten, C2 an C3 und C3 an den Host zur¨uckgeben.
```
Die Docker Datei ist unter Dockerfile zu finden und dessen erstelltes Image wird wie folgt gebaut     
`docker build . -t myapp_image`    
Und im Docker Compose wird das Image benutzt und die Umgebungsvariablen so gesetzt, dass eine Kette der Kommunikation wie folgt stattfindet:     
Host -> C1 -> C2 -> C3 -> Host, wobei die Operationen wie in der Aufgabe gesetzt wurden    
```
Container C1 und C3 sollen jeweils sich ein Port mit dem
Host teilen, damit der Host mit den Containern kommunizieren kann.
Der Host teilt sich port 9000 mit C1 und 9001 mit C3. Container
C2 soll nur mit C1 und C3 kommunizieren. ¨Uber die geteilten Ports
soll der Host mit C1 und C3 mit dem Host kommunizieren k¨onnen.
Passen Sie das Docker Compose-File entsprechend an.
Schicken Sie nun Daten an C1 und ¨uberpr¨ufen Sie, ob die Ergebnisse
korrekt sind. Verwenden Sie nc um UDP-Pakete an C1 zu senden und
die Ergebnisse von C3 zu empfangen
```
Diese Funktionalität wurde zusammen mit Q2 mit den Umgebungsvariablen realisiert, die
Ergebnisse wurden mit Socat überprüft, die Kommunikation funktioniert.     
Es existiert allerdings nur ein Problem auf dem Mac Rechner, da Docker in einer VM läuft, muss die IP vom Mac     
als `SENDING_ADDRESS` für den C3 Container verwendet werden, sonst empfängt der Host keine Nachrichten.     
# Q4
```
Ersetzen Sie die UDP-Sockets durch TCP-Sockets in Ih-
rem Programm. ¨Uberpr¨ufen Sie, ob die Kommunikation zwischen
den Containern und dem Host weiterhin funktioniert. Erstellen Sie
ein neues Docker-Image mit dem Namen myapp tcp image und ver-
wenden Sie es in Ihrem Docker Compose-File.
```
Die Q4 Aufgabe wurde auf dem Branch "tcp" dieses Repos erledigt. Auf dem Branch kann der Code gesehen werden.
Die Kommunikation funktioniert, sofern der Host sich mit C1 verbindet und mit C3 eine Verbindung akzeptiert. Sonst fließt nichts.    

# Q5
```
Beschreiben Sie kurz
- die Unterschiede zwischen der Verwendung von TCP und UDP
in Ihrem Programm in Bezug auf die Containerkommunikation
und Start-up Verhalten.
- Warum eignet sich reine TCP/UDP Kommunikation nicht f¨ur
verteilte Systeme?
```
1. Das Startup Verhalten bei dem UDP Programm ist uneingeschränkt. Es kann Pakete senden, ohne 
dass eine "angemeldete" Gegenseite lauschen muss. Das Programm startet, wartet auf Pakete (blocking)
und nachdem er Pakete empfängt, verarbeitet er diese und sendet sie weiter. Das Programm überprüft nicht
ob die Pakete ankommen oder nicht, nur die Richtigkeit der Ports und Adressen. Das Programm kann aber auch nicht
gewährleisten, dass die Pakete angekommen sind. Bei TCP ist dies eine andere Geschichte,
das Programm startet und wartet auf eingehende Verbindung, danach öffnet es eine Verbindung mit dem Client / Empfänger,
wenn keine eingehende Verbindung kommt, wird auch keine Verbindung mit dem Client gemacht und das Programm wartet.
Das heisst, dass das Startup Verhalten beim TCP Programm stark von eingehenden / ausgehenden aufgebauten Verbindungen beeinflusst wird.
2. UDP ist nicht geeignet, da es unzuverlässig ist; wir wissen nicht, ob unsere Pakete empfangen wurden, ob
die Gegenseite alles empfangen hat, ob unsere Pakete in der richtigen Reihenfolge ankommen, es existiert keine Fehlerbehandlung.    
TCP löst die o.g. Probleme, trotzdem ist es ungeeignet, da es zu einfach ist. Uns sind lediglich die IP Adresse und der Port bekannt,    
es existieren keine Unterscheidungen zwischen Crash und Verbindungsabbruch, in einem Netz müsste jeder Knoten mit jedem Knoten
eine TCP Verbindung aufmachen, da TCP nur eine one on one Verbindung unterstützt; es gibt kein Broadcast / Multicast.
