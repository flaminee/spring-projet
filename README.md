

## Lancer le projet

`./run-hsqldb-server.sh` <BR>
`./show-hsqldb.sh `

URL : `jdbc:hsqldb:hsql://localhost/`

`run SampleDataJpaApplication`

## Lien swagger

http://localhost:8080/swagger-ui/



## Example de requête
### Register prof :

#### Header
Content-Type: application/json <Br>
#### Body
{"name":"Tom","email":"dupond@gmail.com","password":"mdp"}


### Create appointment :
#### Header
Authorization: Token {tokenUser} <br>
Content-Type: application/json
#### Body
{"start":"2022-10-02 15:30:00.123000","end":"2022-10-02 17:30:00.123000","title":"appointment"}

## Choix de conception

Deux type d'utilisateur :
- des professionnels qui peuvent créer ou supprimer des rendez-vous
- Des cleint qui peuvent s'inscrire ou se désinscrir des rendez-vous

Pour réaliser ses actions les utilisateurs doivent être connectés
car seuls les profs sont autorisés à créer des rendez-vous et seuls les clients peuvent s'inscrire. <br>

Le token de l'utilisateur est envoyé en réponse des requêtes login ou register.

Pour effectuer des requêtes sur les rendez-vous, la ligne ci-dessous doit être rajouté dans l'header le requête avec le token de l'utilisateur : 
`Authorization: Token {tokenUser}`