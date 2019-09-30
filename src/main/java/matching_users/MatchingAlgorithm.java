package matching_users;

import block_match.BlockedMatch;
import block_match.BlockedMatchBuilder;
import interests.UserInterest;
import interests.UserInterestBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import languages.UserLanguage;
import languages.UserLanguageBuilder;
import user.ObeoUser;

public class MatchingAlgorithm {
    private static Map<String, Integer> interestToNumber = new HashMap<>();
    public static double PERFECT_MATCH_SCORE = 5;
    public static double PARTIAL_MATCH_SCORE = 3;
    public static double REPORT_DECREMENT = 2;

    public static double MOST_COMFY_DISTANCE = 0.25;
    public static double LEAST_COMFY_DISTANCE = 5.25;
    public static double DISTANCE_MAX_SCORE = 20;
    public static double DISTANCE_MIN_SCORE = 0;
    public static double DISTANCE_OFFSET = 21;
    public static double GRADIENT = 4;

    public static int INTEREST_RANGE = 10;
    public static int LANGUAGE_MULTIPLIER = 20;

    static {
        interestToNumber.put("Football", 1);
        interestToNumber.put("Rugby", 2);
        interestToNumber.put("Squash", 3);
        interestToNumber.put("Badminton", 4);
        interestToNumber.put("Basketball", 5);
        interestToNumber.put("Tennis", 6);
        interestToNumber.put("Cricket", 7);
        interestToNumber.put("Netball", 8);
        interestToNumber.put("Volleyball", 9);
        interestToNumber.put("Hip-hop", 10);
        interestToNumber.put("Jazz", 11);
        interestToNumber.put("Pop", 12);
        interestToNumber.put("Grime", 13);
        interestToNumber.put("Rap", 14);
        interestToNumber.put("Classical", 15);
        interestToNumber.put("Disco", 16);
        interestToNumber.put("Horror", 21);
        interestToNumber.put("Action", 22);
        interestToNumber.put("Mystery", 23);
        interestToNumber.put("Drama", 24);
        interestToNumber.put("Romance", 25);
        interestToNumber.put("Comedy", 26);
        interestToNumber.put("Thriller", 27);
        interestToNumber.put("Sci-Fi", 28);
        interestToNumber.put("Poetry", 31);
        interestToNumber.put("Non-Fiction", 32);
        interestToNumber.put("Fantasy", 33);
        interestToNumber.put("Murder-Mystery", 34);
        interestToNumber.put("Manga", 35);
        interestToNumber.put("Comics", 36);
        interestToNumber.put("Sci-Fi Novels", 37);
        interestToNumber.put("Salsa", 41);
        interestToNumber.put("Contemporary", 42);
        interestToNumber.put("Street Dance", 43);
        interestToNumber.put("Ballet", 44);
        interestToNumber.put("Ballroom", 45);
        interestToNumber.put("Break Dance", 46);
        interestToNumber.put("Indian", 51);
        interestToNumber.put("Japanese", 52);
        interestToNumber.put("Chinese", 53);
        interestToNumber.put("French", 54);
        interestToNumber.put("American", 55);
        interestToNumber.put("Spanish", 56);
        interestToNumber.put("Thai", 57);
        interestToNumber.put("Korean", 51);




    }

    static public double getTotalScore(ObeoUser local, ObeoUser tourist, double distance) {
//        System.out.println(tourist.getFirst_name());
//        System.out.println("interest: " + getInterestScore(local, tourist));
//        System.out.println("language: "+ getLanguageScore(local, tourist));
//        System.out.println("distance: " + getDistanceScore(distance));

        double score = getInterestScore(local, tourist) + getLanguageScore(local, tourist) + getDistanceScore(distance);
        score = score - (REPORT_DECREMENT * BlockedMatchBuilder.numberOfReported(local.getId()) -
                REPORT_DECREMENT * BlockedMatchBuilder.numberOfReported(tourist.getId()));
        return score;
    }

    static public double getLanguageScore(ObeoUser local, ObeoUser tourist) {
        List<UserLanguage> localLevelFour = UserLanguageBuilder.getLanguagesForSpecificUserAndLevel(local.getId(), 4);
        List<UserLanguage> localLevelFive = UserLanguageBuilder.getLanguagesForSpecificUserAndLevel(local.getId(), 5);
        List<UserLanguage> localSufficientLanguages = new ArrayList<>();
        localSufficientLanguages.addAll(localLevelFour);
        localSufficientLanguages.addAll(localLevelFive);

        List<UserLanguage> touristLevelFour = UserLanguageBuilder.getLanguagesForSpecificUserAndLevel(tourist.getId(), 4);
        List<UserLanguage> toursitLevelFive = UserLanguageBuilder.getLanguagesForSpecificUserAndLevel(tourist.getId(), 5);
        List<UserLanguage> touristSufficientLanguages = new ArrayList<>();
        touristSufficientLanguages.addAll(touristLevelFour);
        touristSufficientLanguages.addAll(toursitLevelFive);

        Set<UserLanguage> sharedLanguage = localSufficientLanguages.stream().filter(touristSufficientLanguages::contains).collect(Collectors.toSet());

        return sharedLanguage.size() * LANGUAGE_MULTIPLIER;

    }
    static public double getInterestScore(ObeoUser local, ObeoUser tourist) {
        List<UserInterest> localInterests = UserInterestBuilder.getInterestsForUserid(local.getId());


        List<Integer> localMapped = localInterests.stream().map
            (i -> interestToNumber.get(i.getInterest())).collect(Collectors.toList());

        List<UserInterest> touristInterest = UserInterestBuilder.getInterestsForUserid(tourist.getId());
        List<Integer> touristMapped = touristInterest.stream().map
            (i -> interestToNumber.get(i.getInterest())).collect(Collectors.toList());

        double score = 0;
        List<Integer> lowerBoundPartialMatches = new ArrayList<>();

        for (Integer l : localMapped ) {
            System.out.println("Mapping " + l);
            int lowerBound = l/INTEREST_RANGE;
            int upperBound = lowerBound + INTEREST_RANGE;

            List<Integer> touristFiltered  = touristMapped.stream().
                filter(i -> (i > lowerBound && i < upperBound)).collect(Collectors.toList());
            List<Integer> localFiltered  = localMapped.stream().
                filter(i -> (i > lowerBound && i < upperBound)).collect(Collectors.toList());

//            if (touristMapped.contains(l)) {
//                System.out.println("Adding " + PERFECT_MATCH_SCORE);
//                score += PERFECT_MATCH_SCORE;
//                // compare to other tourst mapped interest;
//                System.out.println("Adding " + (PARTIAL_MATCH_SCORE/(localFiltered.size()-1)));
//
//                score += (PARTIAL_MATCH_SCORE/(localFiltered.size()-1));
//            } else {
//
//                if (!touristFiltered.isEmpty()) {
//                    System.out.println("Adding " + (PARTIAL_MATCH_SCORE/localFiltered.size()));
//
//                    score += (PARTIAL_MATCH_SCORE/localFiltered.size());
//                }
//            }

            if (touristMapped.contains(l)) {
                System.out.println("PERFECT MATCH");
                score += PERFECT_MATCH_SCORE;
                if (touristFiltered.size() > 1 && !lowerBoundPartialMatches.contains(lowerBound)) {
                    score += PARTIAL_MATCH_SCORE;
                    lowerBoundPartialMatches.add(lowerBound);
                }
            } else {
                if (!touristFiltered.isEmpty() && !lowerBoundPartialMatches.contains(lowerBound)) {
                    System.out.println("PARTIAL MATCH");
                    score += (PARTIAL_MATCH_SCORE);
                    lowerBoundPartialMatches.add(lowerBound);
                }
            }

        }

        return score;
    }

    private static double getDistanceScore(double distance) {
        if (distance < MOST_COMFY_DISTANCE) {
            return DISTANCE_MAX_SCORE;
        } else if (distance > LEAST_COMFY_DISTANCE) {
            return DISTANCE_MIN_SCORE;
        } else {
            return DISTANCE_OFFSET - (GRADIENT * distance);
        }
    }

}
