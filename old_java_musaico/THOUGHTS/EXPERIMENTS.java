public class League
{
    public String name;
    public Sequence<Season> seasons;
    public Sequence<Player> players;
    public Sequence<Team> teams;
}

public class Season
{
    public String name;
    public Sequence<Game> regularSeason;
    public Sequence<Game> playoffs;
    public Table<Team,Roster> rosters;
}

public class Game
{
    public Roster homeTeam;
    public Roster awayTeam;
    ...box scores etc...
}

public class Roster
{
    public Team team;
    public Sequence<Player> players;
}

public class Player
{
    public String name;
}

public class Team
{
    public String name;
}




MyMusaicoDistribution musaico = new MyMusaicoDistribution ();

final League hfhl = new League ();
musaico.bin.mount ( musaico.obj.GNOSYS, hfhl, musaico.dev.db.hfhl1 );
musaico.environment.set ( musaico.env.TERMINAL, musaico.term.webBrowser );
musaico.bin.ls ( hfhl );  --> Displays league name and links to "seasons" etc
musaico.bin.cat ( hfhl );  --> Displays league name and link to each season etc
    musaico.bin.find ( hfhl ).exec ( musaico.bin.grep ( "Johann" ).list () ); --> Displays every object / flat record containing "Johann" in the data (e.g. a Player card)
