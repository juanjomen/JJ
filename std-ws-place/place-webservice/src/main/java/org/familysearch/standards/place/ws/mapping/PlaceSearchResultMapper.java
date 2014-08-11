package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.familysearch.standards.core.Localized;
import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.ResultMetadata;
import org.familysearch.standards.place.Scoring;
import org.familysearch.standards.place.data.PlaceNameDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.scoring.Scorecard;
import org.familysearch.standards.place.scoring.Scorer;
import org.familysearch.standards.place.search.RequestMetrics;
import org.familysearch.standards.place.search.interp.Interpretation;
import org.familysearch.standards.place.search.parse.ParsedToken;
import org.familysearch.standards.place.search.spatial.SpatialResultMetadata;
import org.familysearch.standards.place.util.PlaceHelper;
import org.familysearch.standards.place.ws.model.CentroidModel;
import org.familysearch.standards.place.ws.model.CountsModel;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.LocationModel;
import org.familysearch.standards.place.ws.model.MetricsModel;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.model.PlaceSearchResultModel;
import org.familysearch.standards.place.ws.model.ScorerModel;
import org.familysearch.standards.place.ws.model.ScorersModel;
import org.familysearch.standards.place.ws.model.TimingsModel;
import org.familysearch.standards.place.ws.model.TokenMatch;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;

public class PlaceSearchResultMapper {
	private TypeMapper							typeMapper;
	private GroupMapper                         groupMapper;
	private CommonMapper                        commonMapper;


	public PlaceSearchResultMapper( ReadablePlaceDataService theService ) {
		typeMapper = new TypeMapper( theService );
		groupMapper = new GroupMapper( theService );
		commonMapper = new CommonMapper( theService );
	}

	public PlaceSearchResultModel createSearchResultFromPlaceRepresentation( PlaceRepresentation rep, StdLocale locale, String path, boolean includeMetrics ) {
		PlaceSearchResultModel				model = new PlaceSearchResultModel();
		PlaceRepresentationModel			repModel = new PlaceRepresentationModel();
		NameModel							displayName = new NameModel();
		NameModel							fullDisplayName = new NameModel();
		PlaceRepresentation[]				chain;
		ResultMetadata						metadata;
		Scoring								scoring;
		List<LinkModel>						links = new ArrayList<LinkModel>( 2 ); //only two links being added
		LinkModel							link;
		Localized<String>					name;
		String								nameStr;
		String								localeStr;

		//Extract the display name.
		name = rep.getDisplayName( locale );
		displayName.setName( name.get() );
		displayName.setLocale( name.getLocale().toString() );

		//Extract the full display name.
		name = rep.getFullDisplayName( locale );
		nameStr = name.get();
		localeStr = name.getLocale().toString();
		fullDisplayName.setName( nameStr );
		fullDisplayName.setLocale( localeStr );

		//Copy from the place rep to the model.
		repModel.setId( rep.getId() );
		repModel.setUUID( rep.getUUID() );
		repModel.setRevision( rep.getRevision() );
		repModel.setType( typeMapper.createModelFromType( rep.getType(), locale, path, TypeCategory.PLACE ) );
		repModel.setDisplayName( displayName );
		repModel.setFullDisplayName( fullDisplayName );
		if ( rep.getJurisdictionToYear() != Integer.MAX_VALUE ) {
			repModel.setToYear( rep.getJurisdictionToYear() );
		}
		if ( rep.getJurisdictionFromYear() != Integer.MIN_VALUE ) {
			repModel.setFromYear( rep.getJurisdictionFromYear() );
		}
		repModel.setOwnerId( rep.getPlaceId() );
		if ( rep.getTypeGroup() != null ) {
            PlaceTypeGroupModel group = groupMapper.createModelFromTypeGroup(rep.getTypeGroup(), path);
            repModel.setGroup( group );
		}
		repModel.setPreferredLocale( rep.getPreferredLocale().toString() );
		repModel.setPublished( rep.isPublished() );
		repModel.setValidated( rep.isValidated() );
		if ( rep.getCentroidLatitude() != null && rep.getCentroidLongitude() != null ) {
			LocationModel						location = new LocationModel();
			CentroidModel						centroid = new CentroidModel();

			centroid.setLatitude( rep.getCentroidLatitude() );
			centroid.setLongitude( rep.getCentroidLongitude() );
			location.setCentroid( centroid );

			repModel.setLocation( location );
		}

		//Handle the atom links
		link = new LinkModel();
		link.setRel( "self" );
		link.setTitle( nameStr );
		link.setHref( path + PlaceRepresentationMapper.REPS_PATH + rep.getId() );
		links.add( link );
		link = new LinkModel();
		link.setRel( "related" );
		link.setHref( path + PlaceMapper.PLACE_PATH + rep.getPlaceId() );
		links.add( link );

		repModel.setLinks( links );

		//Handle the jurisdiction chain
		chain = rep.getJurisdictionChain();
		if ( chain.length > 1 ) {
			repModel.setJurisdiction( commonMapper.createJurisdictionModelFromJurisdictionChain( chain[ 1 ], locale, path ) );
		}

		metadata = rep.getMetadata();
		if ( metadata != null ) {
			SpatialResultMetadata		spatialData;
			Interpretation				interp;

			spatialData = metadata.getSpatialMetatdata();
			scoring = metadata.getScoring();
			interp = metadata.getInterpretation();
			if ( scoring != null ) {
				model.setRawScore( scoring.getRawScore() );
				model.setRelevanceScore( scoring.getRelevanceScore() );
			}
			if ( spatialData != null ) {
				Double			distance;

				distance = spatialData.getDistanceFromCentroid();
				if ( distance != null ) {
					model.setDistanceInKM( distance );
					model.setDistanceInMiles( PlaceHelper.convertKMToMiles( distance ) );
				}
			}
			if ( interp != null ) {
				PlaceRepresentation[]	reps;
				List<ParsedToken>		tokens;
				List<TokenMatch>		tokenMatches = new ArrayList<TokenMatch>();
				PlaceNameDTO[]			nameMatches;
				
				tokens = interp.getParsedInput().getTokens();
				reps = interp.getTokenIndexMatches();
				nameMatches = interp.getMatchedVariants();

				//Note that the length of the token list and the token matches are
				//assumed to match.  If they do not, then there's a programming
				//error somewhere, since they are supposed to be the same, by definition.
				for ( int i = 0; i < reps.length; i++ ) {
					TokenMatch			match;

					match = new TokenMatch();
					match.setToken( tokens.get( i ).getOriginalToken() );
					if ( reps[ i ] != null ) {
						match.setId( reps[ i ].getId() );
					}
					if ( nameMatches[ i ] != null ) {
						NameModel		nameMatch;

						//TODO:  Need to add the type of the name?
						nameMatch = new NameModel();
						nameMatch.setLocale( nameMatches[ i ].getName().getLocale().toString() );
						nameMatch.setName( nameMatches[ i ].getName().get() );
						match.setNameMatch( nameMatch );
					}
					tokenMatches.add( match );
				}
				model.setTokenMatches( tokenMatches );


				if ( includeMetrics ) {
					Scorecard			card;

					card = interp.getScorecard();
					if ( card != null ) {
						ScorersModel		scorersModel = new ScorersModel();
						List<ScorerModel>	scorersList = new ArrayList<ScorerModel>();
						Set<Scorer>			scorers;

						scorers = card.getScorersThatScored();
						for ( Scorer scorer : scorers ) {
							ScorerModel		scorerModel = new ScorerModel();
	
							scorerModel.setName( scorer.getClass().getSimpleName() );
							scorerModel.setScore( card.getScoreFromScorer( scorer ) );
							scorerModel.setReason( card.getScoreReason( scorer ) );
							scorersList.add( scorerModel );
						}
						scorersModel.setScorers( scorersList );
						model.setScorers( scorersModel );
					}
				}
			}
		}
		model.setRep( repModel );

		return model;
	}

	public MetricsModel createModelFromMetrics( RequestMetrics metrics ) {
		MetricsModel				model = new MetricsModel();
		TimingsModel				timings = new TimingsModel();
		CountsModel					counts = new CountsModel();
		ScorersModel				scorers = new ScorersModel();
		Set<Scorer>					theScorers;
		List<ScorerModel>			scorerList = new ArrayList<ScorerModel>();

		model.setTimings( timings );
		model.setCounts( counts );
		model.setScorers( scorers );

		//Set the data on the timings model
		timings.setParseTime( metrics.getParseTime() );
		timings.setIdentifyCandidatesTime( metrics.getIdentifyCandidatesTime() );
		timings.setIdentifyCandidatesLookupTime( metrics.getIdentifyCandidateLookupTime() );
		timings.setIdentifyCandidatesMaxHitFilterTime( metrics.getIdentifyCandidateMaxHitFilterTime() );
		timings.setIdentifyCandidatesTailMatchTime( metrics.getIdentifyCandidateTailMatchTime() );
		timings.setScoringTime( metrics.getScoringTime() );
		timings.setAssemblyTime( metrics.getAssemblyTime() );
		timings.setTotalTime( metrics.getTotalTime() );

		//Set the data on the counts model
		counts.setRawCandidateCount( metrics.getRawCandidateCount() );
		counts.setPreScoringCandidateCount( metrics.getPreScoringCandidateCount() );
		counts.setInitialParsedInputTextCount( metrics.getInitialParsedInputTextCount() );
		counts.setFinalParsedInputTextCount( metrics.getFinalParsedInputTextCount() );

		//Set the data for the scorers model
		theScorers = metrics.getTimedScorers();
		for ( Scorer scorer : theScorers ) {
			Long		time;
			ScorerModel	scorerModel;

			scorerModel = new ScorerModel();

			time = metrics.getScorerTime( scorer );
			scorerModel.setTime( time );
			scorerModel.setName( scorer.getClass().getSimpleName() );

			scorerList.add( scorerModel );
		}
		scorers.setScorers( scorerList );

		return model;
	}
}
