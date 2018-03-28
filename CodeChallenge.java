/*
 * Programmed by Adam Walker
 * adamwalker567123@gmail.com
 */

import java.util.ArrayList;
import java.util.Scanner;
public class CodeChallenge
{
	static class ChefGame 
	{
		class Chef
		{
			String z;
			int mpm;
			double q;
			String f;
		}
		class Night
		{
			int m;
			double gq;
			int t;
		}

		int k;
		Scanner in;

		ArrayList<Chef> chefs = new ArrayList<Chef>();
		
		//each team, reset each night
		ArrayList<Chef> teamA = new ArrayList<Chef>();
		ArrayList<Chef> teamB = new ArrayList<Chef>();
		
		//each night
		ArrayList<Night> nights = new ArrayList<Night>();

		//output string if there is a winner
		String output = "";

		int currNight = 0;
		double currBestSum = 0;

		//basically the driver to run everything
		void go()
		{
			in = new Scanner(System.in);
			k = in.nextInt();
			getInput();
			for(currNight = 0; currNight < k-1; currNight++)
			{
				if(!canCookBeforeTeaming())
				{
					System.out.println("No winner");
					System.exit(0);
				}
				formTeams();
				if(!canCookMeals())
				{
					System.out.println("No winner");
					System.exit(0);
				}
				eliminateChef();
			}
			output += (chefs.get(0).z);
			System.out.println(output);
		}

		//before making a team, check these conditions
		boolean canCookBeforeTeaming()
		{
			int mpmT = 0;

			//k in bounds
			if(k > 100 || k < 1)
				return false;
			
			//nights arguments in bounds
			for(Night n : nights)
			{
				if(n.gq > 1.000 || n.gq < 0.000 ||
						n.m > 10000 || n.m < 1 ||
						n.t > 10000 || n.t < 1)
				{
					return false;
				}
			}
			
			//chefs' arguments in bounds
			for(Chef c : chefs)
			{
				if(/*c.mpm > 5 ||*/ c.mpm < 1 ||
						c.q > 1.000 || c.q < 0.000)
				{
					//System.out.println("Chef MPM: " + c.mpm);
					return false;
				}
				mpmT += c.mpm;
			}

			//making that many meals is possible with any combo of teams
			for(int c = 0; c < nights.size(); c++)
			{
				//fixed #6
				//must be <1/2 because this will be 
				//the threoretical max of the worse team
				if(mpmT/2 < (double) nights.get(c).m / nights.get(c).t)
					return false;
			}

			//all checks success, return true
			return true;
		}
		
		//makes sure each meal has the ability to cook the meals
		boolean canCookMeals()
		{
			boolean can = true;
			double mpmA = 0, mpmB = 0;
			
			//total mpm's for each team
			for(Chef a : teamA)
			{
				mpmA += a.mpm;
			}
			for(Chef a : teamB)
			{
				mpmB += a.mpm;
			}

			//make sure the meals can be made
			for(int c = currNight; c < nights.size(); c++)
			{
				if(!(mpmA >= (double) nights.get(c).m / nights.get(c).t))
					can = false;
				if(!(mpmB >= (double) nights.get(c).m / nights.get(c).t))
					can = false;
				//if(!(mpmB + mpmA >= nights.get(c).m / nights.get(c).t))
				//	can = false;
			}

			return can;
		}
		
		//gets the input from the screen
		void getInput()
		{
			//read chefs
			for(int i = 0; i < k; i++)
			{
				String z1 = in.next();
				int mpm1 = in.nextInt();
				double q1 = in.nextDouble();
				String f = in.next();
				Chef a = new Chef();
				a.z = z1;
				a.mpm = mpm1;
				a.q = q1;
				a.f = f;
				chefs.add(a);
			}

			//read nights
			for(int i = 0; i < k-1; i++)
			{
				Night n = new Night();
				n.m = in.nextInt();
				n.gq = in.nextDouble();
				n.t = in.nextInt();
				nights.add(n);
			}
		}

		//driver for the recursion to form the teams
		void formTeams()
		{
			currBestSum = 0;
			ArrayList<Chef> cteamA = new ArrayList<Chef>();
			ArrayList<Chef> cteamB = new ArrayList<Chef>();

			formTestTeam(cteamA, cteamB, 0);
			//System.out.print("Team A: ");
			teamA = sortNames(teamA);
			teamB = sortNames(teamB);
			for(Chef a : teamA)
				output += (a.z + " ");
			//System.out.print("\nTeam B: ");
			output += '\n';
			for(Chef a : teamB)
				output += (a.z + " ");
			output += '\n';
			//System.out.println("\nSum: " + currBestSum);
		}

		//takes a team and reorders all the elements to be in alpha by Z arg
		ArrayList<Chef> sortNames(ArrayList<Chef> team)
		{
			ArrayList<Chef> teamS = new ArrayList<Chef>();
			for(int i = 0; i < team.size(); i++)
			{
				String teamName = team.get(i).z;
				//System.out.println(teamName);
				boolean placed = false;
				//System.out.println(teamS.size());
				for(int j = 0; j < teamS.size(); j++)
				{
					//System.out.println(j);
					if(teamS.get(j).z.compareTo(teamName) > 0)
					{
						teamS.add(j, team.get(i));
						placed = true;
						break;
					}
				}
				if(!placed)
					teamS.add(team.get(i));

				//System.out.print("\nALPHA: ");
				//for(Chef a : teamS)
				//	System.out.print(a.z);
				//System.out.println();
			}

			return teamS;
		}

		//uses recursion/brute force to make the best team A
		void formTestTeam(ArrayList<Chef> cteamA, ArrayList<Chef> cteamB, int i3)
		{
			ArrayList<Chef> currteamA = new ArrayList<Chef>(), currteamB = new ArrayList<Chef>();

			//add all to temp teams so that args passed are preserved
			for(int i = 0; i < cteamA.size();i ++)
				currteamA.add(cteamA.get(i));
			for(int i = 0; i < cteamB.size();i ++)
				currteamB.add(cteamB.get(i));

			//ends recursion if teams are full
			if(i3 >= chefs.size())
			{
				if((Math.abs(cteamA.size() - cteamB.size()) <= 1) && cteamA.size() > 0 && cteamB.size() > 0)
				{
					//System.out.println("OUTPUT");
					//System.out.println(cteamA.size() + " " + cteamB.size());

					//team full, test sum
					double sum = getSum(cteamA);

					if(sum > currBestSum)
					{
						//this team is the best found so far, keep it
						currBestSum = sum;
						teamA = cteamA;
						teamB = cteamB;
					}
				}

				/*System.out.print("Team A: ");
			for(Chef a : cteamA)
				System.out.print(a.z + " ");
			System.out.print("\nTeam B: ");
			for(Chef a : cteamB)
				System.out.print(a.z + " ");
			System.out.println("\nSum: " + currBestSum);*/
			}
			else
			{
				//recursion not done yet
				
				//try adding to A, if A isn't filled
				if(cteamA.size() <= chefs.size() / 2)
				{
					currteamA.add(chefs.get(i3));
					formTestTeam(currteamA, currteamB, i3 + 1);
				}

				//reset current team variables
				currteamA = new ArrayList<Chef>();
				currteamB = new ArrayList<Chef>();

				for(int i = 0; i < cteamA.size();i ++)
					currteamA.add(cteamA.get(i));
				for(int i = 0; i < cteamB.size();i ++)
					currteamB.add(cteamB.get(i));

				//try adding to B, if B isn't filled
				if(cteamB.size() <= chefs.size() / 2)
				{
					currteamB.add(chefs.get(i3));
					formTestTeam(currteamA, currteamB, i3 + 1);
				}
			}

		}

		//gets the sum of a given team
		double getSum(ArrayList<Chef> cteam)
		{
			//keeps track if a friend exists for the bonus
			boolean [] hasFriend = new boolean[cteam.size()];
			
			//for each team member...
			for(int i = 0; i < cteam.size(); i++)
			{
				//if there's a friend, see if the friend is on the team, and if so, log that
				String f = cteam.get(i).f;
				if(f != "*")
				{
					for(int j = 0; j < cteam.size(); j++)
					{
						if(f.equals(cteam.get(j).z))
						{
							if(i != j)//no egos
								hasFriend[i] = true;
							//hasFriend[j] = true;
						}
					}
				}
			}

			//finds the average Q and total MPM of the team
			int mpmSum = 0;
			double sumQ = 0;
			for(int i = 0; i < cteam.size(); i++)
			{
				//if(hasFriend[i])
				//	System.out.println(cteam.get(i).z + " has a friend");
				mpmSum += cteam.get(i).mpm + (hasFriend[i] ? 1 : 0);
				sumQ += cteam.get(i).q * (hasFriend[i] ? 1.2 : 1);
			}
			double aveQ = sumQ / cteam.size();
			
			//calculates the bonus if applicable
			int bonus = (aveQ >= nights.get(currNight).gq ? 3 : 1);

			//return the sum
			return aveQ * mpmSum * bonus;
		}

		//eliminates the worst chef from team B
		void eliminateChef()
		{
			//index of worst chef found
			int minChef = 0;
			//value of the worst chef found (mpm * q)
			double minVal = teamB.get(0).mpm * teamB.get(0).q;

			//for each chef in the team (starting with 2nd chef)
			for(int i = 1; i< teamB.size(); i++)
			{
				//find it's val
				double currVal = teamB.get(i).mpm * teamB.get(i).q;
				
				//see if it's worse than any found so far
				if(currVal < minVal)
				{
					//set it as the worst
					minChef = i;
					minVal = currVal;
				}
				//tied with worst found so far. compare the q's
				if(currVal == minVal && teamB.get(i).q < teamB.get(minChef).q)
				{
					minChef = i;
					//minVal = currVal;
				}
			}

			//actually eliminate the chef. find the chef in the chefs array
			for(int i = 0; i < chefs.size(); i++)
			{
				if(chefs.get(i).z.equals(teamB.get(minChef).z))
				{
					//System.out.println("\nEliminated: " + (chefs.remove(i)).z);
					
					//remove the chef at that index and add that chef's name to the output
					output += (chefs.remove(i)).z + "\n";
					break;
				}
			}
		}
	}
	
	//main / driver
	public static void main(String [] args)
	{
		//create the game and start it
		ChefGame a = new ChefGame();
		a.go();
	}
}
