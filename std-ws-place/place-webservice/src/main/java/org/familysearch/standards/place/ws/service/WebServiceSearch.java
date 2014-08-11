package org.familysearch.standards.place.ws.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.GenealogicalDate;
import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.PlaceRequest;
import org.familysearch.standards.place.PlaceRequestBuilder;
import org.familysearch.standards.place.PlaceResults;
import org.familysearch.standards.place.PlaceService;
import org.familysearch.standards.place.PlaceType;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.util.PlaceHelper;
import org.familysearch.standards.place.ws.mapping.PlaceSearchResultMapper;
import org.familysearch.standards.place.ws.model.PlaceSearchRequestModel;
import org.familysearch.standards.place.ws.model.PlaceSearchResultModel;
import org.familysearch.standards.place.ws.model.PlaceSearchResultsModel;
import org.familysearch.standards.place.ws.model.RootModel;


/**
 * The "places" web service provides endpoints to support the searching and retrieval of place and related
 * resources.  The "search" endpoint provides the ability to search for Place Representation entities by
 * using a variety of optional search parameters.  Additional endpoints provide direct access to Place
 * and Place Representation resources, as well as Place Types, Variant Name Types, and Type Groups.
 * 
 * @author dshellman
 */
@Path(WebServicePlaceRep.URL_ROOT + "/" + WebServiceBase.SEARCH_ROOT)
public class WebServiceSearch extends WebServiceBase {


	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceSearch() { }

	/**
	 * This is the primary search end point.  This will cause a single query/search
	 * to occur and will return zero or more Place Representations based upon the search
	 * criteria.  All parameters are optional.  Depending upon the parameters passed,
	 * different types of search queries are generated in the back-end.  There are currently
	 * three basic types of queries:  Place name interpretation, spatial search, and the
	 * general search.
	 * 
	 * The Place name interpretation's primary parameter is the text parameter.  The goal
	 * of this search is to interpret the provided text and come up with the best
	 * "interpretations" of that text as possible.  Other parameters can be provided to
	 * help provide context to that interpretation, such as a date, type, or even a
	 * centroid (lat/long).  Each result is given a score, which represents the
	 * relative strength that result has in being an accurate interpretation compared to
	 * the other results.  Results are returned in order of this score, with the highest
	 * coming first.  Filtering adjusts which of those results are actually returned (see
	 * the filter and threshold parameters).
	 * 
	 * A spatial search's primary parameter is the centroid (lat and lng parameters).  In
	 * addition, the distance parameter defines the radius from the centroid.  All
	 * Place Representations found within that radius are returned (assuming they meet
	 * the other parameters provided, such as date or type).
	 * 
	 * The general search does not have a primary parameter, though typically it
	 * leverages the required parent or indirect parent jurisdiction.  This search is
	 * used to perform searches such as "find all cemeteries in New York."
	 * 
	 * Though all parameters are optional, they apply to different types of searches,
	 * though if a parameter doesn't apply to a particular search, it is just ignored.
	 * 
	 * @param uriInfo URI contextual info.
	 * @param text The text represents the place name, and will be interpreted based
	 * 		  upon the text and the other provided parameters.  The text can include
	 * 		  any characters (note that they may need to URL encoded), though certain
	 * 		  characters may have special meaning.  For example, if the 'wildcards'
	 * 		  parameter is true, then the asterisk and the question mark are treated
	 * 	      as wildcards (the asterisk means zero or more characters and the question
	 * 		  mark means one character).  This parameter is the primary parameter for
	 * 		  an interpretation search, so other parameters, when used with this
	 * 		  parameter, are secondary and constrain the results.  (Interpretation-only)
	 * @param reqYears Specifies a year or year range (e.g. 1850, or 1850-1900).  Resulting
	 * 		  place representations must have existed at the given year or within the year
	 * 		  range.  If no results existed at that year, this parameter may be ignored
	 * 		  (if done during an interpretation, but will not be ignored for the other
	 * 		  request types). (all search types)
	 * @param optYears Specifies a year or year range (e.g. 1850, or 1850-1900).
	 * 		  Resulting place representations that existed at the given year or within
	 * 		  the year range are scored higher (will show up higher in the results). (Interpretation-only)
	 * @param optParents Represents a list of optional place representation identifiers
	 * 		  (comma-delimited).  If included, the place representations returned that
	 * 		  have the specified place representation as a parent in their jurisdiction
	 * 		  chain will be scored higher than those that do not.  (Interpretation-only)
	 * @param optDirParents Acts the same way as the optParents parameter, except that
	 * 		  the identifiers refer to direct parentage (i.e. immediate jurisdiction).
	 * 		  For example, if 'text=provo' and the optDirParents contains the identifier
	 * 		  for Utah (the state), the Provo will not be scored higher.  However, if the
	 * 		  parameter contains the identifier for Utah (the county), then it will.  (Interpretation-only)
	 * @param filterParents Filters out any result candidates that are identified in the
	 * 		  provided comma-delimited list of place representation identifiers
	 * 		  (or children of any of those places).  For example, if the identifier for
	 * 		  the United States is provided, then no results will be returned that are
	 * 		  either a child of the United States or the United States, itself.  (Interpretation-only)
	 * @param filter (default: true) If set to true, those results with the highest score are
	 * 		  returned and all other results are thrown away.  (Interpretation-only)
	 * @param limit (default: 1000) Limits the number of results.  Regardless of other settings, no
	 * 		  more than the specified limit will be returned.  A value of zero ('0')
	 * 		  will not limit results (though the ultimate max limit is 10,000).
	 * 		  Note that a limit of zero could impact performance of the query.  (all search types)
	 * @param threshold (default: 0) The percentage (as an integer) of the top scoring
	 * 		  results to return.  During an interpretation, each candidate is assigned
	 * 		  a score.  The results are sorted by this score.  This parameter is used to
	 * 		  filter out results that have scores that are NOT within a certain percentage
	 * 		  of the top score.  For example, if the top score (across all results) is 100,
	 * 		  and the threshold is 10, then only those results that have a score of 90
	 * 		  and above will be returned. (Interpretation-only)
	 * @param profile (default: default) Specifies the configured profile to use.  This is
	 * 		  an advanced usage parameter and should not normally be used. (Interpretation-only)
	 * @param acceptLanguage Used to override the Accept-Language header.  It specifies the
	 * 		  language (using BCP 47 format) that the results should be in.  A "best try"
	 * 		  approach is used to provide the requested language.  Note that not all text
	 * 		  may exist in all languages.  (all search types)
	 * @param headerAcceptLang It specifies the language (using BCP 47 format) that the
	 * 		  results should be in.  A "best try" approach is used to provide the requested
	 * 		  language.  Note that not all text may exist in all languages.  (all search types)
	 * @param lang Specifies the language of the place name being passed in using the text
	 * 		  parameter.  The format follows BCP 47 (e.g. 'en' for English and 'en_US' for
	 * 		  US English. See the BCP itself for a reference about language code usage.
	 * 		  See the wikipedia article for a list of ISO 639.1 two-letter language codes.
	 * 	   	  Note that not all language codes are currently supported).   This overrides
	 * 		  the language header field. (Interpretation-only)
	 * @param headerLang The language of the request data.
	 * @param reqTypes Specifies one or more required place types as a comma-delimited
	 * 		  list of place identifiers.  Resulting place representations are required
	 * 		  to be one of the specified place types.  (all search types)
	 * @param optTypes Specifies a list of comma-delimited optional place types.
	 * 		  Results are scored higher if they are one of the specified place types. (Interpretation-only)
	 * @param priorityTypes Specifies priorities for specific place types.  The
	 * 		  format of this parameter is:  <place type identifier>=<1-10 priority>,<place type identifier>=<1-10 priority>.
	 * 		  Note that a lower priority value is better (i.e. "higher priority"),
	 * 		  which means that those results that have a place type that is a higher
	 * 		  priority (lower number), will score higher. (Interpretation-only)
	 * @param filterTypes Specifies a comma-delimited list of place types that will
	 * 		  filter out result candidates based upon their place type.  If a candidate
	 * 		  place representation has a type that is specified in this parameter, it
	 * 		  will be removed.  For example, if the filter type is a cemetery, then
	 * 		  all places that have a type of cemetery will be removed and not returned
	 * 		  as part of the results.  (all search types)
	 * @param reqTypeGroups Specifies one or more required type groups.  Works the same
	 * 		  way as the reqTypes parameter, but specifies type groups instead of individual types. (all search types)
	 * @param filterTypeGroups Specifies one or more type groups that will filter our
	 * 		  results.  Works the same way as the filterTypes parameter, except that
	 * 		  this specifies whole type groups, so a place with a type that belongs to
	 * 		  one of the groups will be filtered out.  (all search types)
	 * @param partial (default: false) If set to true, the last token is used in a
	 * 		  "starts-with" query (i.e. like a wildcard:  'token*').  For example,
	 * 		  if the last token in the 'text' parameter is 'pro', then Provo and
	 * 		  Providence will both be returned.  This parameter is only used if the
	 * 		  text parameter is specified.  Note that the use of this parameter may
	 * 		  have a negative performance impact.  This parameter is useful for
	 * 		  "type-ahead" applications.  (Interpretation-only)
	 * @param lng Specifies the longitude of the centroid to use (see additional
	 * 		  details on the 'lat' parameter).  (Interpretation and Spatial search)
	 * @param lat Specifies the latitude of the centroid to use.  Note that if a 'text'
	 * 		  parameter is not specified, then this parameter will be used as a
	 * 		  proximity spatial search (e.g. find all places within 5 kilometers of
	 * 		  the centroid).  If the 'text' parameter is specified, it scores higher
	 * 		  those places closer to the specified centroid and removes those results
	 * 		  that are not within the radius specified by the distance parameter.  (Interpretation and Spatial search)
	 * @param points The list of lat/long points to search within.  Not current supported!
	 * @param distance (default: 5k) Specifies the distance from the centroid specified
	 * 		  in the 'lat' and 'lng' parameters (i.e. sphere created by the radius
	 * 		  and centroid).  The value is considered in kilometers.  However, to use
	 * 		  miles, add an 'm' at the end of the value.  Adding a 'k' will cause the
	 * 		  number to be interpreted as a kilometer, which is the default. (Interpretation and Spatial search)
	 * @param metrics True/False on whether to include search metrics in the results.
	 * @param fuzzy Identifies the type of fuzzy search to use.  If not specified, no
	 * 		  fuzzy search will be used.  The "fuzziness" is in the place name passed
	 * 		  in as the text parameter.  Valid values: ED_40 (edit distance with 40%
	 * 		  threshold), ED_50, ED_60, ED_70, ED_80, and ED_90.  (Interpretation-only)
	 * @param reqParents A list of place representation identifiers (comma-delimited),
	 * 		  this parameter limits the results by requiring that each result belongs
	 * 		  to at least one of the specified place representations (meaning, their
	 * 		  jurisdiction contains the specified place).  For example, if 'text=provo'
	 * 		  and the reqParents parameter contains the identifier for Utah, then the
	 * 		  result will contain Provo in Utah (i.e. Provo, Utah, Utah, United States),
	 * 		  but not Provo in Serbia.  (all search types)
	 * @param reqDirParents Acts the same way as the reqParents parameter, except that
	 * 		  the identifiers refer to direct parentage (i.e. immediate jurisdiction).
	 * 		  For example, if 'text=provo' and the reqDirParents contains the identifier
	 * 		  for Utah (the state), then Provo will not be returned.  However, if the
	 * 		  parameter contains the identifier for Utah (the county), then it will.  (all search types)
	 * @param wildcards (default: true) Specifies whether the asterisk and question mark
	 * 		  are used as wildcards during an interpretation or not.  The asterisk is
	 * 		  used to represent zero or more characters.  The question mark is used
	 * 		  to represent a single character.  (Interpretation-only)
	 * @param version Specifies the name of a version that represents the set of data
	 * 		  to query against.  If not specified, the latest changes are used in the
	 * 		  query.  Note that this parameter is used to specify a version of the place
	 * 		  data, not the interface or schema of the endpoint. (all search types)
	 * @param pubType (default: pub_only) Specifies whether places must be published or
	 * 		  not in order to be a part of the query.  There are three possible values:
	 * 		  pub_only means that only published places will be returned in the results;
	 * 		  non_pub_only means that only non-published places will be returned in the
	 * 		  results; pub_non_pub means that both published and non-published places will
	 * 		  be returned.  Normally, callers should ONLY use published place data.
	 * 		  Published places are those places that are designed to be made publicly available.
	 * 		  Typically, non-published places are NOT useable or interesting to use by
	 * 	 	  client applications or patrons. (all search types)
	 * @param valType (default: val_non_val) Specifies whether places must be validated
	 * 		  or not in order to be a part of the query. There are three possible values:
	 * 		  val_only means that only validated places will be returned in the results;
	 * 		  non_val_only means that only non-validated places will be returned; val_non_val
	 * 		  means both validated and non-validated places will be returned.  Validated
	 * 		  places are those places that have been reviewed and verified via an
	 * 		  authoritative source.  Non-validated places are considered to be in a queue
	 * 		  waiting for review.  Once reviewed, they will either be validated or deleted
	 * 		  (deleted means that they will be pointed to another place, so the identifier
	 * 		  will still be usable...it will merely point to a different identifier). (all search types)
	 * 
	 * @return Returns zero or more place representation results based upon the search criteria provided.
	 */
	@GET
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	public Response requestPlaceReps( @Context UriInfo uriInfo, 
									   @QueryParam("text") String text,
									   @QueryParam("reqYears") String reqYears,
									   @QueryParam("optYears") String optYears,
									   @QueryParam("optParents") String optParents,
									   @QueryParam("optDirParents") String optDirParents,
									   @QueryParam("filterParents") String filterParents,
									   @DefaultValue("false") @QueryParam("filter") boolean filter,
									   @DefaultValue("1000") @QueryParam("limit") int limit,
									   @DefaultValue("0") @QueryParam("threshold") int threshold,
									   @DefaultValue(PlaceService.DEFAULT_PROFILE) @QueryParam("profile") String profile,
									   @QueryParam("accept-language") String acceptLanguage,
									   @HeaderParam("Accept-Language") String headerAcceptLang,
									   @QueryParam("language") String lang,
									   @HeaderParam("Language") String headerLang,
									   @QueryParam("reqTypes") String reqTypes,
									   @QueryParam("optTypes") String optTypes,
									   @QueryParam("priorityTypes") String priorityTypes,
									   @QueryParam("filterTypes") String filterTypes,
									   @QueryParam("reqTypeGroups") String reqTypeGroups,
									   @QueryParam("filterTypeGroups") String filterTypeGroups,
									   @DefaultValue("false") @QueryParam("partial") Boolean partial,
									   @QueryParam("lng") Double lng,
									   @QueryParam("lat") Double lat,
									   @QueryParam("points") String points,
									   @DefaultValue("5") @QueryParam("distance") String distance,
									   @DefaultValue("false") @QueryParam("metrics") boolean metrics,
									   @QueryParam("fuzzy") String fuzzy,
									   @QueryParam("reqParents") String reqParents,
									   @QueryParam("reqDirParents") String reqDirParents,
									   @DefaultValue("true") @QueryParam("wildcards") boolean wildcards,
									   @QueryParam("version") String version,
									   @DefaultValue("pub_only") @QueryParam("pubType") String pubType,
									   @DefaultValue("val_non_val") @QueryParam("valType") String valType,
									   @DefaultValue("false") @QueryParam("strict") boolean strict ) {
		PlaceService					service;
		PlaceRepresentation[]			reps;
		PlaceRequestBuilder				request;
		StdLocale						inLocale;
		StdLocale						outLocale;
		RootModel						root = new RootModel();
		PlaceSearchResultsModel			searchResults = new PlaceSearchResultsModel();
		List<PlaceSearchResultsModel>	searchResultsList = new ArrayList<PlaceSearchResultsModel>();
		List<PlaceSearchResultModel>	resultsList = new ArrayList<PlaceSearchResultModel>();
		PlaceResults					results;
		PlaceSearchResultMapper			mapper;
		long							startTime;
		String							path;
		
		startTime = System.currentTimeMillis();

		service = getPlaceService( profile );

		//Figure out what language they want the info back in.
		outLocale = getLocale( acceptLanguage, headerAcceptLang );

		//Figure out what language they sent the info in with.
		inLocale = getLocale( lang, headerLang );

		//Get the path that will be used for atom links.
		path = getPath( uriInfo );

		request = new PlaceRequestBuilder();
		//Make sure we get the version first, since other params may depend upon it.
		handleParamVersion( service, request, version );
		handleParamText( service, request, text, inLocale );
		handleParamRequiredDate( service, request, reqYears );
		handleParamOptionalDate( service, request, optYears );
		handleParamOptionalParent( service, request, optParents );
		handleParamOptionalDirectParent( service, request, optDirParents );
		handleParamRequiredTypes( service, request, reqTypes );
		handleParamOptionalTypes( service, request, optTypes );
		handleParamFilterTypes( service, request, filterTypes );
		handleParamFilterPlaces( service, request, filterParents );
		handleParamFilterTypeGroups( service, request, filterTypeGroups );
		handleParamRequiredTypeGroups( service, request, reqTypeGroups );
		handleParamPriorityTypes( service, request, priorityTypes );
		handleParamResultsLimit( service, request, limit );
		handleParamFilter( service, request, filter );
		handleParamThreshold( service, request, threshold );
		handleParamPartialInput( service, request, partial );
		handleParamCentroid( service, request, lng, lat );
		handleParamDistance( service, request, distance );
		handleParamFuzzyType( service, request, fuzzy );
		handleParamRequiredParent( service, request, reqParents );
		handleParamRequiredDirectParent( service, request, reqDirParents );
		handleParamPoints( service, request, points );
		handleParamIncludeMetrics( service, request, metrics );
		handleParamWildcards( service, request, wildcards );
		handleParamPublished( service, request, pubType );
		handleParamValidated( service, request, valType );
		handleParamStrict( service, request, strict );

		//Make the actual search request.
		mapper = new PlaceSearchResultMapper( getReadableService() );
		results = service.requestPlaces( request.getRequest() );
		reps = results.getPlaceRepresentations();
		for ( PlaceRepresentation rep : reps ) {
			PlaceSearchResultModel			result;

			result = mapper.createSearchResultFromPlaceRepresentation( rep, outLocale, path, request.shouldCollectMetrics() );
			resultsList.add( result );
		}
		searchResults.setResults( resultsList );
		searchResults.setRefId( 1 );
		searchResults.setCount( resultsList.size() );
		if ( request.shouldCollectMetrics() ) {
			searchResults.setMetrics( mapper.createModelFromMetrics( results.getMetrics() ) );
		}
		searchResultsList.add( searchResults );
		root.setSearchResults( searchResultsList );

		log( null, uriInfo.getPath(), "GET",
					"time", String.valueOf( ( System.currentTimeMillis() - startTime ) ),
					"text", text,
					"reqYears", reqYears,
					"optYears", optYears,
					"optParentIds", optParents,
					"optDirParents", optDirParents,
					"filter", String.valueOf( filter ),
					"limit", String.valueOf( limit ),
					"threshold", String.valueOf( threshold ),
					"profile", profile,
					"accept-language", acceptLanguage,
					"Accept-language", headerAcceptLang,
					"language", lang,
					"Language", headerLang,
					"reqTypes", reqTypes,
					"optTypes", optTypes,
					"priorityTypes", priorityTypes,
					"filterTypes", filterTypes,
					"reqTypeGroups", reqTypeGroups,
					"filterTypeGroups", filterTypeGroups,
					"partial", String.valueOf( partial ),
					"lng", String.valueOf( lng ),
					"lat", String.valueOf( lat ),
					"points", points,
					"distance", String.valueOf( distance ),
					"metrics", String.valueOf( metrics ),
					"fuzzy", fuzzy,
					"wildcards", String.valueOf( wildcards ),
					"reqParents", reqParents,
					"reqDirParents", reqDirParents,
					"pubType", pubType,
					"valType", valType );

		return Response.ok( root ).expires( this.getExpireDate() ).build();
	}

	/**
	 * This end point handles multiple search requests at the same time.  This is the "bulk" search
	 * end point.
	 * 
	 * @param inRoot
	 * @param headerAcceptLang
	 * @param headerLang
	 * @return
	 */
	@POST
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	@Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
	public Response requestPlaceReps( @Context UriInfo uriInfo,
									   RootModel inRoot,
									   @HeaderParam("Accept-Language") String headerAcceptLang,
									   @HeaderParam("Language") String headerLang ) {
		PlaceService					service;
		RootModel						outRoot = new RootModel();
		List<PlaceSearchRequestModel>	requests;
		List<PlaceSearchResultsModel>	resultsList = new ArrayList<PlaceSearchResultsModel>();
		PlaceSearchResultMapper			mapper;
		long							startTime;
		String							path;

		startTime = System.currentTimeMillis();

		//TODO:  What about the profile to use?
		//The profile will come in on each individual request, which means that
		//theoretically, a different profile could be used per request.
		service = getPlaceService();

		path = getPath( uriInfo );

		requests = inRoot.getRequests();

		mapper = new PlaceSearchResultMapper( getReadableService() );

		//Note:  This could, theoretically, be handled by a thread-pool to speed
		//up the request.
		for ( PlaceSearchRequestModel request : requests ) {
			PlaceRequestBuilder				builder;
			PlaceRepresentation[]			reps;
			PlaceSearchResultsModel			searchResults = new PlaceSearchResultsModel();
			List<PlaceSearchResultModel> 	searchResultList = new ArrayList<PlaceSearchResultModel>();
			PlaceResults					results;
			StdLocale						inLocale;
			StdLocale						outLocale;

			inLocale = getLocale( request.getLanguage(), headerLang );
			outLocale = getLocale( request.getAcceptLanguage(), headerAcceptLang );
			builder = constructRequestFromRequestWrapper( request, service, inLocale );
			results = service.requestPlaces( builder.getRequest() );
			reps = results.getPlaceRepresentations();
			for ( PlaceRepresentation rep : reps ) {
				searchResultList.add( mapper.createSearchResultFromPlaceRepresentation( rep, outLocale, path, builder.shouldCollectMetrics() ) );
			}
			searchResults.setRefId( request.getId() );
			searchResults.setCount( searchResultList.size() );
			searchResults.setResults( searchResultList );
			if ( builder.shouldCollectMetrics() ) {
				searchResults.setMetrics( mapper.createModelFromMetrics( results.getMetrics() ) );
			}

			resultsList.add( searchResults );
		}

		outRoot.setSearchResults( resultsList );

		log( null, uriInfo.getPath(), "POST", "time", String.valueOf( ( System.currentTimeMillis() - startTime ) ) );

		return Response.ok( outRoot ).build();
	}


	protected void handleParamText(PlaceService service, PlaceRequestBuilder request, String text, StdLocale locale) {
		if (text != null  &&  !text.trim().equals("")) {
			request.setText(text, locale);
		}
	}

	protected void handleParamRequiredDate(PlaceService service, PlaceRequestBuilder request, String date) {
		if (date != null) {
			request.setRequiredDate(GenealogicalDate.getInstance(date));
		}
	}

	protected void handleParamOptionalDate(PlaceService service, PlaceRequestBuilder request, String date) {
		if (date != null) {
			request.setOptionalDate(GenealogicalDate.getInstance(date));
		}
	}

	protected void handleParamOptionalParent(PlaceService service, PlaceRequestBuilder request, String optionalId) {
		try {
			if (optionalId != null) {
				String[] ids = optionalId.split(",");
				for (String id : ids) {
					request.addOptionalParent(service.getPlaceRepresentation(Integer.parseInt(id.trim()), request.getVersion()));
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse optional parent id", "optionalId", optionalId);
		}
	}

	protected void handleParamOptionalDirectParent(PlaceService service, PlaceRequestBuilder request, String optionalId) {
		try {
			if (optionalId != null) {
				String[] ids = optionalId.split(",");
				for (String id : ids) {
					request.addOptionalDirectParent(service.getPlaceRepresentation(Integer.parseInt(id.trim()), request.getVersion()));
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse optional direct parent id", "optionalId", optionalId);
		}
	}

	protected void handleParamRequiredTypes(PlaceService service, PlaceRequestBuilder request, String requiredTypes) {
		try {
			if (requiredTypes != null  &&  !requiredTypes.equals("")) {
				String[] types = requiredTypes.split(",");

				//Note that each type may be the type id or it might be the type code,
				//so, the first thing to do is to figure out which one.
				for (String type : types) {
					Integer			typeId;
					TypeDTO			typeDto;

					type = type.trim();
					try {
						typeId = Integer.valueOf(type);
						typeDto = getReadableService().getTypeById(TypeCategory.PLACE, typeId);
					} catch (Exception e) {
						typeDto = getReadableService().getTypeByCode(TypeCategory.PLACE, type);
					}

					if (typeDto != null) {
						request.addRequiredPlaceType(new PlaceType(typeDto));
					} else {
						log(null, SEARCH_ROOT, "GET", "user_error", "Invalid required place type (will be ignored).", "requiredTypes", type);
					}
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse required types", "requiredTypes", requiredTypes);
		}
	}

	protected void handleParamOptionalTypes(PlaceService service, PlaceRequestBuilder request, String optionalTypes) {
		try {
			if (optionalTypes != null  &&  !optionalTypes.equals("")) {
				String[] types = optionalTypes.split(",");
				for (String type : types) {
					Integer  typeId;
					TypeDTO  typeDto;

					type = type.trim();
					try {
						typeId = Integer.valueOf(type);
						typeDto = getReadableService().getTypeById(TypeCategory.PLACE, typeId);
					} catch (Exception e) {
						typeDto = getReadableService().getTypeByCode(TypeCategory.PLACE, type);
					}

					if (typeDto != null) {
						request.addOptionalPlaceType(new PlaceType(typeDto));
					} else {
						log(null, SEARCH_ROOT, "GET", "user_error", "Invalid optional place type (will be ignored).", "optionalTypes", type);
					}
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse optional types", "optionalTypes", optionalTypes);
		}
	}

	protected void handleParamFilterTypes(PlaceService service, PlaceRequestBuilder request, String filterTypes) {
		try {
			if (filterTypes != null) {
				String[] types = filterTypes.split(",");
				for (String type : types) {
					Integer			typeId;
					TypeDTO			typeDto;

					type = type.trim();
					try {
						typeId = Integer.valueOf(type);
						typeDto = getReadableService().getTypeById(TypeCategory.PLACE, typeId);
					} catch (Exception e) {
						typeDto = getReadableService().getTypeByCode(TypeCategory.PLACE, type);
					}

					if (typeDto != null) {
						request.addFilteredPlaceType(new PlaceType(typeDto));
					} else {
						log(null, SEARCH_ROOT, "GET", "user_error", "Invalid filtered place type (will be ignored).", "filterTypes", type);
					}
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse filtered types", "filterTypes", filterTypes);
		}
	}

	protected void handleParamFilterPlaces(PlaceService service, PlaceRequestBuilder request, String filterPlaces) {
		try {
			if (filterPlaces != null) {
				String[] ids = filterPlaces.split(",");
				for (String id : ids) {
					request.addFilterParent(service.getPlaceRepresentation(Integer.parseInt(id.trim()), request.getVersion()));
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse filtered places", "filterPlaces", filterPlaces);
		}
	}

	protected void handleParamFilterTypeGroups(PlaceService service, PlaceRequestBuilder request, String filterTypeGroups) {
		try {
			if (filterTypeGroups != null) {
				String[] types = filterTypeGroups.split(",");
				for (String type : types) {
					request.addFilteredPlaceTypeGroup(service.getPlaceTypeGroupById(Integer.parseInt(type.trim())));
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse filtered type groups", "filterTypeGroups", filterTypeGroups);
		}
	}

	protected void handleParamRequiredTypeGroups(PlaceService service, PlaceRequestBuilder request, String requiredTypeGroups) {
		try {
			if (requiredTypeGroups != null) {
				String[] types = requiredTypeGroups.split(",");
				for (String type : types) {
					request.addRequiredPlaceTypeGroup(service.getPlaceTypeGroupById(Integer.parseInt(type.trim())));
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse required type groups", "requiredTypeGroups", requiredTypeGroups);
		}
	}

	protected void handleParamPriorityTypes(PlaceService service, PlaceRequestBuilder request, String priorityTypes) {
		try {
			if (priorityTypes != null) {
				//Format for this parameter is as follows:
				//type=priority,type=priority
				//PPL=3,CMTY=8
				String[] types = priorityTypes.split(",");
				for (String typeToPriority : types) {
					String[]		typePriority;
					Integer			typeId;
					Integer			priority;
					TypeDTO			typeDto;
	
					typePriority = typeToPriority.split(":");
					if (typePriority.length != 2) {
						log(null, SEARCH_ROOT, "GET", "user_error", "Invalid priority type format.", "priorityTypes", typeToPriority);
						continue;
					}

					typePriority[ 0 ] = typePriority[ 0 ].trim();
					typePriority[ 1 ] = typePriority[ 1 ].trim();
					try {
						priority = Integer.valueOf(typePriority[ 1 ]);
						if (priority < 0  ||  priority > 10) {
							throw new Exception("Priority must be between 0 and 10.");
						}
					} catch (Exception e) {
						log(e, SEARCH_ROOT, "GET", "user_error", "Invalid priority.", "priorityTypes", typeToPriority);
						continue;
					}

					try {
						typeId = Integer.valueOf(typePriority[ 0 ]);
						typeDto = getReadableService().getTypeById(TypeCategory.PLACE, typeId);
					} catch (Exception e) {
						typeDto = getReadableService().getTypeByCode(TypeCategory.PLACE, typePriority[ 0 ]);
					}

					if (typeDto != null) {
						request.setPriorityForPlaceType(new PlaceType(typeDto), priority);
					} else {
						log(null, SEARCH_ROOT, "GET", "user_error", "Invalid priority place type (will be ignored).", "priorityTypes", typeToPriority);
					}
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse priority types", "priorityTypes", priorityTypes);
		}
	}

	protected void handleParamResultsLimit(PlaceService service, PlaceRequestBuilder request, int limit) {
		request.setResultsLimit(limit);
	}

	protected void handleParamFilter(PlaceService service, PlaceRequestBuilder request, boolean filterFlag) {
		if (filterFlag == true) {
			request.setFilterResults(filterFlag);
		}
	}

	protected void handleParamThreshold(PlaceService service, PlaceRequestBuilder request, int threshold) {
		if (threshold > 0) {
			request.setFilterThreshold(threshold);
		}
	}

	protected void handleParamPartialInput(PlaceService service, PlaceRequestBuilder request, boolean partialInput) {
		request.setPartialInput(partialInput);
	}

	protected void handleParamCentroid(PlaceService service, PlaceRequestBuilder request, Double longitude, Double latitude) {
		if (longitude != null  &&  latitude != null) {
			request.setCentroid(longitude, latitude);
		}
	}

	protected void handleParamDistance(PlaceService service, PlaceRequestBuilder request, String distance) {
		try {
			if (distance != null  &&  !distance.trim().equals("")) {
				distance = distance.trim();
				if (distance.endsWith("k")) {
					request.setDistanceInKM(new Double(distance.substring(0, distance.length() - 1)));
				} else if (distance.trim().endsWith("m")) {
					request.setDistanceInKM(PlaceHelper.convertMilesToKM(new Double(distance.substring(0, distance.length() - 1))));
				} else {
					request.setDistanceInKM(new Double(distance));
				}
			}
		} catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse distance", "distance", distance);
		}
	}

	protected void handleParamFuzzyType(PlaceService service, PlaceRequestBuilder request, String fuzzyType) {
		if (fuzzyType != null  &&  fuzzyLookup.get(fuzzyType.trim().toLowerCase()) != null) {
			request.setFuzzyType(fuzzyLookup.get(fuzzyType.trim().toLowerCase()));
		}
	}

	protected void handleParamRequiredParent(PlaceService service, PlaceRequestBuilder request, String reqParent) {
		try {
			if (reqParent != null) {
				String[] parents = reqParent.split(",");
				for (String str : parents) {
					request.addRequiredParent(service.getPlaceRepresentation(Integer.parseInt(str.trim()), request.getVersion()));
				}
			}
		}
		catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse required parent", "reqParent", reqParent);
		}
	}

	protected void handleParamRequiredDirectParent(PlaceService service, PlaceRequestBuilder request, String reqDirParent) {
		try {
			if (reqDirParent != null) {
				String[] parents = reqDirParent.split(",");
				for (String str : parents) {
					request.addRequiredDirectParent(service.getPlaceRepresentation(Integer.parseInt(str.trim()), request.getVersion()));
				}
			}
		} catch (Exception e) {
			log(e, SEARCH_ROOT, "GET", "user_error", "Failed to parse required direct parent", "reqDirParent", reqDirParent);
		}
	}

	protected void handleParamPoints(PlaceService service, PlaceRequestBuilder request, String points) {
		if (points != null) {
			String[] pointArray = points.split(";");
			for (int i = 0; i < pointArray.length; i++) {
			    String[] coord = pointArray[ i ].split(",");
				request.addPolygonPoint(new Double(coord[ 0 ]), new Double(coord[ 1 ]));
			}
		}
	}

	protected void handleParamIncludeMetrics(PlaceService service, PlaceRequestBuilder request, boolean includeMetrics) {
		request.setShouldCollectMetrics(includeMetrics);
	}

	protected void handleParamWildcards(PlaceService service, PlaceRequestBuilder request, boolean shouldUseWildcards) {
		request.setUseWildcards(shouldUseWildcards);
	}

	protected void handleParamStrict(PlaceService service, PlaceRequestBuilder request, boolean strict) {
		request.setStrictInterpretation(strict);
	}

	protected void handleParamPublished(PlaceService service, PlaceRequestBuilder request, String pubType) {
		PlaceRequest.PublishedType		type;

		if (pubType == null  ||  pubType.trim().equals("")) {
			return;
		}

		type = pubTypes.get(pubType);
		if (type != null) {
			request.setPublishedType(type);
		} else {
			//Basically, this means that the wrong type was sent.  Need to log/error on this.
			log(null, SEARCH_ROOT, "GET", "user_error", "Received an invalid publish type", "pubType", pubType);
		}
	}

	protected void handleParamValidated(PlaceService service, PlaceRequestBuilder request, String validatedType) {
		PlaceRequest.ValidatedType		type;

		if (validatedType == null  ||  validatedType.trim().equals("")) {
			return;
		}

		type = validatedTypes.get(validatedType);
		if (type != null) {
			request.setValidatedType(type);
		} else {
			//Basically, this means that the wrong type was sent.  Need to log/error on this.
			log(null, SEARCH_ROOT, "GET", "user_error", "Received an invalid validated type", "validatedType", "" + validatedType);
		}
	}

	protected void handleParamVersion(PlaceService service, PlaceRequestBuilder request, String version) {
		if (version != null  &&  !version.trim().equals("")) {
			request.setVersion(version.trim());
		}
	}

	protected PlaceRequestBuilder constructRequestFromRequestWrapper(PlaceSearchRequestModel wrapper, PlaceService service, StdLocale inLocale) {
		PlaceRequestBuilder builder = new PlaceRequestBuilder();

		handleParamText(service, builder, wrapper.getText(), inLocale);
		handleParamRequiredDate(service, builder, wrapper.getRequiredYears());
		handleParamOptionalDate(service, builder, wrapper.getOptionalYears());
		handleParamOptionalParent(service, builder, wrapper.getOptionalParents());
		handleParamRequiredTypes(service, builder, wrapper.getRequiredTypes());
		handleParamOptionalTypes(service, builder, wrapper.getOptionalTypes());
		handleParamFilterPlaces(service, builder, wrapper.getFilterParents());
		handleParamFilterTypes(service, builder, wrapper.getFilterTypes());
		handleParamFilterTypeGroups(service, builder, wrapper.getFilterTypeGroups());
		handleParamRequiredTypeGroups(service, builder, wrapper.getReqTypeGroups());
		handleParamPriorityTypes(service, builder, wrapper.getPriorityTypes());
		handleParamResultsLimit(service, builder, wrapper.getLimit());
		handleParamFilter(service, builder, wrapper.getFilter());
		handleParamThreshold(service, builder, wrapper.getThreshold());
		handleParamPartialInput(service, builder, wrapper.getPartial());
		handleParamCentroid(service, builder, wrapper.getLongitude(), wrapper.getLatitude());
		handleParamDistance(service, builder, wrapper.getDistance());
		handleParamFuzzyType(service, builder, wrapper.getFuzzy());
		handleParamRequiredParent(service, builder, wrapper.getRequiredParents());
		handleParamRequiredDirectParent(service, builder, wrapper.getRequiredDirParents());
		handleParamPublished(service, builder, wrapper.getPublishedType());
		handleParamValidated(service, builder, wrapper.getValidatedType());
		handleParamVersion(service, builder, wrapper.getVersion());
		handleParamOptionalDirectParent(service, builder, wrapper.getOptDirParentIds());
		handleParamPoints(service, builder, wrapper.getPoints());
		handleParamIncludeMetrics(service, builder, wrapper.getIncludeMetrics());
		handleParamWildcards(service, builder, wrapper.getUseWildcards());

		return builder;
	}

}
