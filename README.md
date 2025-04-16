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
