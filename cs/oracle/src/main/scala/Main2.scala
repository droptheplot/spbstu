import scala.annotation.tailrec
import scala.util.Random

object Main2 extends App {
  type Score = Int

  case class Team(name: String)
  case class Game(id: Int, awayTeam: Team, awayScore: Score, homeTeam: Team, homeScore: Score, temp: Int) {
    override def toString: String =
      f"Game #$id%03d\t${awayTeam.name} $awayScore%02d : $homeScore%02d ${homeTeam.name}\t${temp}% 3d"
  }
  case class Stat(wins: Long = 0, losses: Long = 0, ties: Long = 0) {
    override def toString: String =
      s"Wins: $wins, Losses: $losses, Ties: $ties"
  }

  val coldWeeksToFinishSeason = 3
  val tempToFinishSeason = 0
  val lowestPossibleScore = 10
  val highestPossibleScore = 20

  val teams = List(
    Team("Team #1"),
    Team("Team #2"),
    Team("Team #3"),
    Team("Team #4"),
  )

  val games = play(List(5), List.empty)

  val stats = games.foldLeft(Map.empty[Team, Stat]) { case (acc, game) =>
    val oldAwayStat = acc.getOrElse(game.awayTeam, Stat())
    val oldHomeStat = acc.getOrElse(game.homeTeam, Stat())

    val (newAwayStat, newHomeStat) = game match {
      case game if game.awayScore > game.homeScore =>
        (oldAwayStat.copy(wins = oldAwayStat.wins + 1), oldHomeStat.copy(losses = oldHomeStat.losses + 1))
      case game if game.awayScore < game.homeScore =>
        (oldAwayStat.copy(losses = oldAwayStat.losses + 1), oldHomeStat.copy(wins = oldHomeStat.wins + 1))
      case _ =>
        (oldAwayStat.copy(ties = oldAwayStat.ties + 1), oldHomeStat.copy(ties = oldHomeStat.ties + 1))
    }

    acc + (game.awayTeam -> newAwayStat, game.homeTeam -> newHomeStat)
  }

  val sumTemp = games.map(_.temp).sum
  val maxTemp = games.map(_.temp).max
  val avgTemp = sumTemp / games.length

  println(games.mkString("\n"))

  println(stats.mkString("\n"))

  println(s"Max temp: $maxTemp")
  println(s"Average temp: $avgTemp")

  @tailrec
  private def play(temps: List[Int], games: List[Game]): List[Game] =
    if (temps.takeRight(coldWeeksToFinishSeason).forall(_ < tempToFinishSeason)) games
    else {
      if (temps.lastOption.contains(tempToFinishSeason)) games
      else {
        val lastGameId = games.lastOption.map(_.id).getOrElse(0)
        val temp = Random.between(-20, 20)
        val (awayTeam, homeTeam) = Random.shuffle(teams).take(2) match {
          case away :: home :: Nil => (away, home)
          case _ => sys.error("Not enough teams to play")
        }

        val game = Game(lastGameId + 1, awayTeam, randomScore(temp), homeTeam, randomScore(temp), temp)

        play(temps :+ temp, games :+ game)
      }
    }

  private def randomScore(temp: Int): Score = {
    val tempFactor = if (temp > 0) temp / 10.0 else 1.0
    Random.between(lowestPossibleScore * tempFactor, highestPossibleScore * tempFactor).toInt
  }
}
