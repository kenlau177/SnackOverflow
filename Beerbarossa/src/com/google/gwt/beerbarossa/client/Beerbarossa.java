package com.google.gwt.beerbarossa.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.Timer;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.beerbarossa.client.LiquorService;
import com.google.gwt.beerbarossa.client.Liquors;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Beerbarossa implements EntryPoint, LatLngCallback {

	CellTable<Liquors> table = null;

	// Create name column.
	TextColumn<Liquors> nameColumn = new TextColumn<Liquors>() {
		@Override
		public String getValue(Liquors object) {
			return object.getProductLongName();
		}
	};

	// Create alcohol % column.
	TextColumn<Liquors> percentageColumn = new TextColumn<Liquors>() {
		@Override
		public String getValue(Liquors object) {
			return Double.toString(object.getAlcoholContent());
		}
	};

	// Create litres column.
	TextColumn<Liquors> litresColumn = new TextColumn<Liquors>() {
		@Override
		public String getValue(Liquors object) {
			return Double.toString(object.getLitres());
		}
	};

	// Create price column.
	TextColumn<Liquors> pricesColumn = new TextColumn<Liquors>() {
		@Override
		public String getValue(Liquors object) {
			return Double.toString(object.getPrice());
		}
	};

	// Create class column.
	TextColumn<Liquors> subclassColumn = new TextColumn<Liquors>() {
		@Override
		public String getValue(Liquors object) {
			return object.getClassname();
		}
	};

	// Main (login/out) stuff:
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the application.");
	private String userName = null;

	// Admin functionality
	private static final String[] admins = { "panzerfaustsarenotfun@gmail.com",
			"ktanyag@gmail.com", "gundalf_the_white@hotmail.com",
			"devonrgraham@gmail.com" };
	private final List<String> listOfAdmins = Arrays.asList(admins);
	private boolean isAdminLoggedIn = false;
	private HorizontalPanel adminPanel = new HorizontalPanel();

	// Buttons for admin:
	private final Button parseliquorsbutton = new Button("Get Liquors Data Set");
	private final Button parselocationsbutton = new Button(
			"Get Locations Data Set");

	// This for data parsing:
	private final LiquorServiceAsync liquorService = GWT
			.create(LiquorService.class);

	private final LocationServiceAsync locationService = GWT
			.create(LocationService.class);

	// This is for notes:
	private FlexTable notesFlexTable = new FlexTable();
	private ArrayList<String> notes = new ArrayList<String>();
	private final NoteServiceAsync noteService = GWT.create(NoteService.class);

	// This is for twitter:
	private Anchor twitterLoginLink;
	private Button twitterLogoutButton = new Button("logout Twitter");
	private Label twitterLabelResult = new Label();
	private final TwitterServiceAsync twitterService = GWT
			.create(TwitterService.class);
	private TwitterInfo twitterInfo = new TwitterInfo();

	// search ui fields
	private VerticalPanel resultsPanel = new VerticalPanel();
	private HorizontalPanel dropPanel = new HorizontalPanel();
	private VerticalPanel notesPanel = new VerticalPanel();
	private TextBox searchTextbox = new TextBox();
	private Button searchButton = new Button("Search");
	private FlexTable liquorFlexTable = new FlexTable();
	private ListBox dropDownPrice = new ListBox();
	private String priceForQuery;
	private Button dropDownButton = new Button("Search");
	private ListBox dropDownName = new ListBox();
	private String nameForQuery;
	private String volumeForQuery;
	private ListBox dropDownVolume = new ListBox();
	private String contentForQuery;
	private ListBox dropDownContent = new ListBox();
	private HorizontalPanel labelPanel = new HorizontalPanel();
	private Label labelForDropDown = new Label("Price   "
			+ "Category          Volume     Alcohol %  ");

	private Grid dropGrid = new Grid(2, 5);

	// maps
	private ArrayList<Locations> listOfLocations = new ArrayList<Locations>();
	private ArrayList<Double> listOfDistances = new ArrayList<Double>();
	private ArrayList<Locations> nearestLocations = new ArrayList<Locations>();

	private ArrayList<Double> nearestDistances = new ArrayList<Double>();
	private static LatLng addressToShowLatLng;
	private boolean validInput;
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private FlexTable mapLegend = new FlexTable();

	// Style stuff
	VerticalPanel flagPanel = new VerticalPanel();
	Image flagBanner = new Image("http://i.imgur.com/zPBUUaI.png?2");

	public void onModuleLoad() {

		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							if (listOfAdmins.contains(loginInfo
									.getEmailAddress())) {
								isAdminLoggedIn = true;
							}
							loadBeerbarossa();
						} else {
							isAdminLoggedIn = false;
							loadLogin();
						}
					}
				});
	}

	private void loadLogin() {

		loadBanner();
		addDivider("Search Liquors", 0);
		addDivider("Search Stores", 1);
		addDivider("", 3);

		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("liquorsPanel").add(loginPanel);

		loadSearch();
		loadMap();
	}

	private void loadBeerbarossa() {
		// Add flag images
		loadBanner();
		addDivider("Search Liquors", 0);
		addDivider("Search Stores", 1);
		addDivider("Enter a Note", 2);
		addDivider("", 3);

		userName = loginInfo.getNickname();
		Label welcomemessage = new Label("Welcome, " + userName
				+ ". It's mighty morphin' liver cirrhosis time. Prost!");
		welcomemessage.setStyleName("labelText");
		HorizontalPanel h = new HorizontalPanel();
		h.add(welcomemessage);
		RootPanel.get("testLabel").add(h);

		signOutLink.setHref(loginInfo.getLogoutUrl());

		initLiquorsDataset();
		initLocationsDataset();
		if (isAdminLoggedIn) {
			adminPanel.add(parseliquorsbutton);
			adminPanel.setSpacing(14);
			adminPanel.add(parselocationsbutton);
			RootPanel.get("adminPanel").add(adminPanel);
		}

		signOutLink.setHref(loginInfo.getLogoutUrl());
		RootPanel.get("loginButton").add(signOutLink);

		loadSearch();

		// Heading for notes:
		Label notesMessage = new Label("The diary of an alcoholic:");
		HorizontalPanel notemessagepanel = new HorizontalPanel();
		notemessagepanel.addStyleName("titles");
		notemessagepanel.add(notesMessage);
		RootPanel.get("notemsg").add(notemessagepanel);
		// Login Components for Twitter
		twitterLoginLink = new Anchor("Login with Twitter", true, "/");
		twitterLoginLink.setVisible(false);
		twitterLogoutButton.setVisible(false);
		HorizontalPanel twitterLoginPanel = new HorizontalPanel();
		twitterLoginPanel.add(twitterLoginLink);
		twitterLoginPanel.add(twitterLogoutButton);
		RootPanel.get("twitterLoginPanel").add(twitterLoginPanel);

		loadNotesInput();
		notesFlexTable.setText(0, 0, "Content");
		notesFlexTable.setText(0, 1, "Date");
		notesFlexTable.setText(0, 2, "Tweet It");
		notesFlexTable.setText(0, 3, "Remove");
		notesFlexTable.setCellPadding(6);
		notesPanel.add(notesFlexTable);
		RootPanel.get("noteResult").add(notesPanel);
		loadNotes();
		twitterLogin();
		loadMap();
	}

	private void loadSearch() {
		// formatFlexTable();

		resultsPanel.add(liquorFlexTable);
		RootPanel.get("resultsPanel").add(resultsPanel);

		loadSearchDropDown();
		loadSearchText();
	}

	private void loadSearchDropDown() {

		final String[] priceItems = { "all", "0-10", "10-20", "20-30", "30-50",
				"50+" };
		for (int i = 0; i < priceItems.length; i++) {
			dropDownPrice.addItem(priceItems[i]);
		}
		final String[] nameItems = { "all", "WINE", "SPIRITS",
				"REFRESHMENT BEVERAGE", "DE-ALCOHOLIZED BEER",
				"DE-ALCOHOLIZED WINE", "CULINARY PRODUCTS", "BEER" };
		for (int i = 0; i < nameItems.length; i++) {
			dropDownName.addItem(nameItems[i]);
		}
		final String[] volumeItems = { "all", "0-0.7", ".7-.8", ".8+" };
		for (int i = 0; i < volumeItems.length; i++) {
			dropDownVolume.addItem(volumeItems[i]);
		}
		final String[] contentItems = { "all", "0-10", "10-20", "20-40", "40+" };
		for (int i = 0; i < contentItems.length; i++) {
			dropDownContent.addItem(contentItems[i]);
		}
		labelPanel.add(labelForDropDown);

		dropGrid.setText(0, 0, "Price");
		dropGrid.setText(0, 1, "Category");
		dropGrid.setText(0, 2, "Volume");
		dropGrid.setText(0, 3, "Alcohol \n Content");
		dropGrid.setWidget(1, 0, dropDownPrice);
		dropGrid.setWidget(1, 1, dropDownName);
		dropGrid.setWidget(1, 2, dropDownVolume);
		dropGrid.setWidget(1, 3, dropDownContent);
		dropGrid.setWidget(1, 4, dropDownButton);
		dropPanel.add(dropGrid);
		RootPanel.get("dropPanel").add(dropPanel);

		priceForQuery = priceItems[0];
		nameForQuery = nameItems[0];
		volumeForQuery = volumeItems[0];
		contentForQuery = contentItems[0];

		dropDownPrice.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = dropDownPrice.getSelectedIndex();
				priceForQuery = priceItems[index];
			}
		});
		dropDownName.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = dropDownName.getSelectedIndex();
				nameForQuery = nameItems[index];
			}
		});
		dropDownVolume.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = dropDownVolume.getSelectedIndex();
				volumeForQuery = volumeItems[index];
			}
		});
		dropDownContent.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = dropDownContent.getSelectedIndex();
				contentForQuery = contentItems[index];
			}
		});

		dropDownButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (table == null) {
					dropDownSearch();
				} else {
					table.setRowCount(0);
					int count = table.getColumnCount();
					for (int i = 0; i < count; i++) {
						table.removeColumn(0);
					}

					dropDownSearch();
				}
			}
		});
	}

	private void loadSearchText() {
		final Panel panel = new FlowPanel();

		// adds a textbox where if you click it, it autoclears the current text
		// in there for you.
		searchTextbox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (searchTextbox.getText() == "Search by SKU or Prod #") {
					searchTextbox.setText("");
				}
			}
		});

		final FormPanel form2 = new FormPanel();
		Panel formElements = new FlowPanel();
		form2.setAction("blank");
		searchTextbox.setVisibleLength(60);
		searchTextbox.addStyleName("textAreaLabels");
		searchTextbox.setText("Search by SKU or Prod #");

		formElements.add(searchButton);
		formElements.add(searchTextbox);

		form2.addSubmitHandler(new SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (table == null) {
					searchLiquors();
				} else {
					table.setRowCount(0);
					int count = table.getColumnCount();
					for (int i = 0; i < count; i++) {
						table.removeColumn(0);
					}
					searchLiquors();
				}
			}
		});

		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form2.submit();
			}
		});

		// Build panels for notes:
		form2.add(formElements);
		panel.add(form2);
		RootPanel.get("searchPanel").add(panel);

	}

	private void dropDownSearch() {

		int temp = liquorFlexTable.getRowCount();
		for (int i = 1; i < temp; i++) {
			liquorFlexTable.removeRow(1);
		}

		dropDownSearch(priceForQuery, nameForQuery, volumeForQuery,
				contentForQuery);
	}

	private void dropDownSearch(String priceForQuery, String nameForQuery,
			String volumeForQuery, String contentForQuery) {

		liquorService.getLiquorsByDropDown(priceForQuery, nameForQuery,
				volumeForQuery, contentForQuery,
				new AsyncCallback<List<Liquors>>() {
					public void onFailure(Throwable caught) {
						Window.alert("No Liquors found.");
					}

					public void onSuccess(List<Liquors> result) {
						displayLiquors(result);
					}
				});
	}

	private void searchLiquors() {
		int temp = liquorFlexTable.getRowCount();
		for (int i = 1; i < temp; i++) {
			liquorFlexTable.removeRow(1);
		}

		String inputText = searchTextbox.getText().toUpperCase().trim();
		searchTextbox.setFocus(true);

		searchTextbox.setText("");

		searchLiquors(inputText);
	}

	private void searchLiquors(String inputText) {

		if (inputText.matches("[0-9]+") && inputText.length() > 2) {

			liquorService.getLiquorsBySkuNo(inputText,
					new AsyncCallback<List<Liquors>>() {
						public void onFailure(Throwable caught) {
							Window.alert("sku No is not found.");
							searchTextbox.selectAll();
						}

						public void onSuccess(List<Liquors> result) {
							displayLiquors(result);
						}
					});

		} else {

			liquorService.getLiquorsByProductLongName(inputText,
					new AsyncCallback<List<Liquors>>() {
						public void onFailure(Throwable caught) {
							Window.alert("product name is not found.");
							searchTextbox.selectAll();
						}

						public void onSuccess(List<Liquors> result) {
							displayLiquors(result);
						}
					});
		}
	}

	private void displayLiquors(List<Liquors> listOfLiquors) {
		if (listOfLiquors.size() == 0) {
			Window.alert("No Liquors found.");
		} else {
			displayLiquor(listOfLiquors);
		}
	}

	private void displayLiquor(List<Liquors> listOfLiquors) {
		table = new CellTable<Liquors>(listOfLiquors.size());

		subclassColumn.setSortable(true);
		nameColumn.setSortable(true);
		percentageColumn.setSortable(true);
		litresColumn.setSortable(true);
		pricesColumn.setSortable(true);

		table.addColumn(subclassColumn, "Type");
		table.addColumn(nameColumn, "Name");
		table.addColumn(percentageColumn, "Alcohol %");
		table.addColumn(litresColumn, "Litres");
		table.addColumn(pricesColumn, "Price in CAD");
		table.setRowCount(listOfLiquors.size(), true);
		table.setVisibleRange(0, listOfLiquors.size());

		// Create a data provider.
		ListDataProvider<Liquors> dataProvider = new ListDataProvider<Liquors>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(table);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget.
		List<Liquors> list = dataProvider.getList();
		for (Liquors liquor : listOfLiquors) {
			list.add(liquor);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<Liquors> columnSortHandler = new ListHandler<Liquors>(list);
		columnSortHandler.setComparator(nameColumn, new Comparator<Liquors>() {
			public int compare(Liquors o1, Liquors o2) {
				if (o1 == o2) {
					return 0;
				}

				// Compare the name columns.
				if (o1 != null) {
					return (o2 != null) ? o1.getProductLongName().compareTo(
							o2.getProductLongName()) : 1;
				}
				return -1;
			}
		});
		table.addColumnSortHandler(columnSortHandler);

		// handler for %
		ListHandler<Liquors> percentageSortHandler = new ListHandler<Liquors>(
				list);
		columnSortHandler.setComparator(percentageColumn,
				new Comparator<Liquors>() {
					public int compare(Liquors o1, Liquors o2) {
						if (o1 == o2) {
							return 0;
						}
						// Compare the name columns.
						if (o1 != null) {
							return (o2 != null) ? Double.valueOf(
									o1.getAlcoholContent()).compareTo(
									Double.valueOf(o2.getAlcoholContent())) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(percentageSortHandler);

		// handler for litres
		ListHandler<Liquors> litreSortHandler = new ListHandler<Liquors>(list);
		columnSortHandler.setComparator(litresColumn,
				new Comparator<Liquors>() {
					public int compare(Liquors o1, Liquors o2) {
						if (o1 == o2) {
							return 0;
						}
						// Compare the name columns.
						if (o1 != null) {
							return (o2 != null) ? Double
									.valueOf(o1.getLitres()).compareTo(
											Double.valueOf(o2.getLitres())) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(litreSortHandler);

		// handler for price
		ListHandler<Liquors> priceSortHandler = new ListHandler<Liquors>(list);
		columnSortHandler.setComparator(pricesColumn,
				new Comparator<Liquors>() {
					public int compare(Liquors o1, Liquors o2) {
						if (o1 == o2) {
							return 0;
						}
						// Compare the name columns.
						if (o1 != null) {
							return (o2 != null) ? Double.valueOf(o1.getPrice())
									.compareTo(Double.valueOf(o2.getPrice()))
									: 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(priceSortHandler);

		// handler for subclass name
		ListHandler<Liquors> subClassHandler = new ListHandler<Liquors>(list);
		columnSortHandler.setComparator(subclassColumn,
				new Comparator<Liquors>() {
					public int compare(Liquors o1, Liquors o2) {
						if (o1 == o2) {
							return 0;
						}
						// Compare the name columns.
						if (o1 != null) {
							return (o2 != null) ? o1.getClassname().compareTo(
									o2.getClassname()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(subClassHandler);

		resultsPanel.add(table);
		RootPanel.get("resultsPanel").add(resultsPanel);

	}

	private void loadMap() {

		final Panel panel = new FlowPanel();

		getListOfLocations(); // assigns listOfLocations

		Timer timer = new Timer() {
			public void run() {
				Maps.loadMapsApi("AIzaSyDcJ1HUe3U5DNBj7tH2Wv2yHzuYvs8Kct8",
						"2", false, new Runnable() {
							public void run() {
								buildUi();
							}

							private void buildUi() {
								LatLng VANCOUVER = LatLng.newInstance(49.2500,
										-123.1000); // map initially centred
													// here
								final Geocoder geocoder;
								final MapWidget map;
								final TextBox address = new TextBox();
								final FormPanel form = new FormPanel();
								Panel formElements = new FlowPanel();

								form.setAction("#");
								map = new MapWidget(VANCOUVER, 13);
								map.setSize("500px", "500px");

								geocoder = new Geocoder();

								address.setVisibleLength(10);
								address.setWidth("73px");
								address.addStyleName("textAreaLabels");
								address.setText("Postal Code");

								Button submit = new Button("Search");

								// CUSTOM MARKER STUFF STARTS
								// create icon for 0-5km
								Icon iconNearRange = Icon
										.newInstance("http://www.ugrad.cs.ubc.ca/~n6o8/markerNearRange.png");
								iconNearRange.setIconSize(Size.newInstance(32,
										32));
								iconNearRange.setIconAnchor(Point.newInstance(
										16, 32));
								iconNearRange.setInfoWindowAnchor(Point
										.newInstance(10, 10)); // changed from
																// 16 1
								final MarkerOptions optionsNearRange = MarkerOptions
										.newInstance();
								optionsNearRange.setIcon(iconNearRange);
								// create icon for 5-10km
								Icon iconMidRange = Icon
										.newInstance("http://www.ugrad.cs.ubc.ca/~n6o8/markerMidRange.png");
								iconMidRange.setIconSize(Size.newInstance(32,
										32));
								iconMidRange.setIconAnchor(Point.newInstance(
										16, 32));
								iconMidRange.setInfoWindowAnchor(Point
										.newInstance(10, 10));
								final MarkerOptions optionsMidRange = MarkerOptions
										.newInstance();
								optionsMidRange.setIcon(iconMidRange);
								// create icon for 10-25km
								Icon iconFarRange = Icon
										.newInstance("http://www.ugrad.cs.ubc.ca/~n6o8/markerFarRange.png");
								iconFarRange.setIconSize(Size.newInstance(32,
										32));
								iconFarRange.setIconAnchor(Point.newInstance(
										16, 32));
								iconFarRange.setInfoWindowAnchor(Point
										.newInstance(10, 10));
								final MarkerOptions optionsFarRange = MarkerOptions
										.newInstance();
								optionsFarRange.setIcon(iconFarRange);

								// CUSTOM MARKER STUFF ENDS

								submit.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										form.submit();

									}
								});
								formElements.add(submit);
								formElements.add(address);

								address.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										if (address.getText() == "Postal Code") {
											address.setText("");
										}
									}
								});

								form.addSubmitHandler(new SubmitHandler() {
									public void onSubmit(SubmitEvent event) {
										showAddress(address.getText());
										event.cancel();
									}

									private void showAddress(
											final String addressToShow) {

										// final InfoWindow info =
										// map.getInfoWindow();

										// Before searching again, clear all
										// arrays and markers from map

										if (!listOfDistances.isEmpty()) {

											nearestLocations.clear();
											nearestDistances.clear();
											listOfDistances.clear();
											map.clearOverlays();
											map.getInfoWindow().close();
											validInput = true;

										}

										geocoder.getLatLng(addressToShow,
												new LatLngCallback() {
													@Override
													public void onFailure() {
														// if gibberish entered
														Window.alert("\""
																+ addressToShow
																+ "\" cannot be found. Please re-enter a postal code.");
														map.clearOverlays();
														validInput = false;
													}

													@Override
													public void onSuccess(
															LatLng point) {
														map.panTo(point);
														Marker marker2 = new Marker(
																point);
														map.addOverlay(marker2);

														validInput = true;
														addressToShowLatLng = point;

													}
												});

										Timer t2 = new Timer() {
											public void run() {

												for (int i = 0; i < listOfLocations
														.size(); i++) {
													double tempLat = listOfLocations
															.get(i).getLat();
													double tempLon = listOfLocations
															.get(i).getLon();
													LatLng tempLatLng = LatLng
															.newInstance(
																	tempLat,
																	tempLon);
													listOfDistances.add(tempLatLng
															.distanceFrom(addressToShowLatLng));

													// Location info null check
													// starts
													if (listOfLocations.get(i)
															.getAddress() == null) {
														listOfLocations
																.get(i)
																.setStoreAddress(
																		"Info not found.");
													}
													if (listOfLocations.get(i)
															.getPostalCode() == null) {
														listOfLocations
																.get(i)
																.setPostalCode(
																		"Info not found.");
													}
													if (listOfLocations.get(i)
															.getPhone() == null) {
														listOfLocations
																.get(i)
																.setStorePhone(
																		"Info not found.");
													}
													if (listOfLocations.get(i)
															.getFax() == null) {
														listOfLocations
																.get(i)
																.setStoreFax(
																		"Info not found.");
													}
													if (listOfLocations.get(i)
															.getCity() == null) {
														listOfLocations
																.get(i)
																.setStoreCity(
																		"Info not found.");
													}
													// Location info null check
													// ends

												}

												for (int j = 0; j < listOfDistances
														.size(); j++) {

													if (listOfDistances.get(j) <= 25000) { // filter
																							// with
																							// threshold
																							// set
																							// at
																							// 25km
														nearestLocations
																.add(listOfLocations
																		.get(j));
														nearestDistances
																.add(listOfDistances
																		.get(j));
													}
												}

												// sort nearestDistances and
												// corresponding
												// nearestLocations
												for (int i = nearestDistances
														.size() - 1; i >= 0; i--) {

													for (int j = 0; j < i; j++) {
														if (nearestDistances
																.get(j) > nearestDistances
																.get(j + 1)) {
															Double temp1 = nearestDistances
																	.get(j);
															nearestDistances
																	.set(j,
																			nearestDistances
																					.get(j + 1));
															nearestDistances
																	.set(j + 1,
																			temp1);
															Locations temp2 = nearestLocations
																	.get(j);
															nearestLocations
																	.set(j,
																			nearestLocations
																					.get(j + 1));
															nearestLocations
																	.set(j + 1,
																			temp2);
														}
													}
												}

												if ((nearestDistances.size() == 0)
														&& validInput) {
													// if no stores within 25km
													Window.alert("Please search again. No BC Liquor Stores found within 25km of "
															+ "\""
															+ addressToShow
															+ "\"");
												}
												if (nearestDistances.size() < 10
														&& validInput) {

													for (int m = 0; m < nearestDistances
															.size(); m++) {
														double tempNearestLat = nearestLocations
																.get(m)
																.getLat();
														double tempNearestLong = nearestLocations
																.get(m)
																.getLon();
														LatLng tempNearestLatLong = LatLng
																.newInstance(
																		tempNearestLat,
																		tempNearestLong);

														// 0-5km = dark green
														// 5-10km = medium green
														// 10-25km = light green
														if (nearestDistances
																.get(m) < 5000) {
															Marker markerNearest = new Marker(
																	tempNearestLatLong,
																	optionsNearRange);
															map.addOverlay(markerNearest);
															handleInfoWindow(
																	markerNearest,
																	nearestLocations
																			.get(m));
														} else if (nearestDistances
																.get(m) < 10000) {
															Marker markerNearest = new Marker(
																	tempNearestLatLong,
																	optionsMidRange);
															map.addOverlay(markerNearest);
															handleInfoWindow(
																	markerNearest,
																	nearestLocations
																			.get(m));
														} else {
															Marker markerNearest = new Marker(
																	tempNearestLatLong,
																	optionsFarRange);
															map.addOverlay(markerNearest);
															handleInfoWindow(
																	markerNearest,
																	nearestLocations
																			.get(m));
														}

													}
												} else {

													if (!(nearestDistances
															.isEmpty())
															&& validInput) { // need
																				// this
																				// check
																				// to
																				// clear
																				// markers
																				// after
																				// gibberish
																				// address
																				// inputted
														for (int n = 0; n < 10; n++) {
															double tempNearestLat = nearestLocations
																	.get(n)
																	.getLat();
															double tempNearestLong = nearestLocations
																	.get(n)
																	.getLon();
															LatLng tempNearestLatLong = LatLng
																	.newInstance(
																			tempNearestLat,
																			tempNearestLong);

															if (nearestDistances
																	.get(n) < 5000) {
																final Marker markerNearest = new Marker(
																		tempNearestLatLong,
																		optionsNearRange);
																map.addOverlay(markerNearest);
																handleInfoWindow(
																		markerNearest,
																		nearestLocations
																				.get(n));
															} else if (nearestDistances
																	.get(n) < 10000) {
																Marker markerNearest = new Marker(
																		tempNearestLatLong,
																		optionsMidRange);
																map.addOverlay(markerNearest);
																handleInfoWindow(
																		markerNearest,
																		nearestLocations
																				.get(n));
															} else {
																Marker markerNearest = new Marker(
																		tempNearestLatLong,
																		optionsFarRange);
																map.addOverlay(markerNearest);
																handleInfoWindow(
																		markerNearest,
																		nearestLocations
																				.get(n));
															}

														}
													}// end if
														// (!(nearestDistances.size....
												}
											} // end of run()

											private void handleInfoWindow(
													final Marker markerNearest,
													final Locations nearestLocations) {
												markerNearest
														.addMarkerClickHandler(new MarkerClickHandler() {

															@Override
															public void onClick(
																	MarkerClickEvent event) {

																String tableContent = "<table>"
																		+ "<TH COLSPAN=\"2\">"
																		+ nearestLocations
																				.getName()
																		+ ", "
																		+ nearestLocations
																				.getCity()
																		+ "</TH>"
																		+ "<tr>"
																		+ "<td>Address</td>"
																		+ "<td>"
																		+ nearestLocations
																				.getAddress()
																		+ ", "
																		+ nearestLocations
																				.getPostalCode()
																		+ "</td>"
																		+ "</tr>"
																		+ "<tr>"
																		+ "<td>Phone Number</td>"
																		+ "<td>"
																		+ nearestLocations
																				.getPhone()
																		+ "</td>"
																		+ "</tr>"
																		+ "<td>Fax Number</td>"
																		+ "<td>"
																		+ nearestLocations
																				.getFax()
																		+ "</td>"
																		+ "</tr>"
																		+ "</table>";

																InfoWindowContent info = new InfoWindowContent(
																		tableContent);

																map.getInfoWindow()
																		.open(markerNearest
																				.getLatLng(),
																				info);

															}

														});

											}

										};// end Timer
										t2.schedule(500);

									} // end of showaddresses
								});
								// Build panels for map
								form.add(formElements);
								panel.add(form);
								panel.add(map);

								mapPanel.add(panel);
								mapPanel.setVerticalAlignment(HasAlignment.ALIGN_BOTTOM);
								buildMapLegend();
								mapPanel.add(mapLegend);
								RootPanel.get("mapPanel").add(mapPanel);

							}
						});
			}
		};

		timer.schedule(5000);
	}

	// Just builds the map legend
	private void buildMapLegend() {

		Image youImage = new Image(
				"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FE7569");
		Image nearImage = new Image(
				"http://www.ugrad.cs.ubc.ca/~n6o8/markerNearRange.png");
		Image midImage = new Image(
				"http://www.ugrad.cs.ubc.ca/~n6o8/markerMidRange.png");
		Image farImage = new Image(
				"http://www.ugrad.cs.ubc.ca/~n6o8/markerFarRange.png");

		mapLegend.setWidth("220px");
		mapLegend.getCellFormatter().setWidth(4, 1, "180px");

		mapLegend.setText(0, 0, "Legend");
		mapLegend.setWidget(1, 0, youImage);
		mapLegend.setWidget(2, 0, nearImage);
		mapLegend.setWidget(3, 0, midImage);
		mapLegend.setWidget(4, 0, farImage);
		mapLegend.setText(1, 1, "This is your location");
		mapLegend.setText(2, 1, "Stores within 5km of your location");
		mapLegend.setText(3, 1, "Stores from 5km to 10km of your location");
		mapLegend.setText(4, 1, "Stores from 10km to 25km of your location");

		mapLegend.getCellFormatter().setStyleName(0, 0, "legendHeader");
		mapLegend.getCellFormatter().setStyleName(1, 0, "legendPinColumn");
		mapLegend.getCellFormatter().setStyleName(2, 0, "legendPinColumn");
		mapLegend.getCellFormatter().setStyleName(3, 0, "legendPinColumn");
		mapLegend.getCellFormatter().setStyleName(4, 0, "legendPinColumn");
		mapLegend.getCellFormatter().setStyleName(0, 1, "legendHeader");
		mapLegend.getCellFormatter().setStyleName(1, 1, "legendTextColumn");
		mapLegend.getCellFormatter().setStyleName(2, 1, "legendTextColumn");
		mapLegend.getCellFormatter().setStyleName(3, 1, "legendTextColumn");
		mapLegend.getCellFormatter().setStyleName(4, 1, "legendTextColumn");

		mapLegend.setCellPadding(3);

	}

	// END TIMER HERE

	// /////// NOTES BEGIN HERE

	private void loadNotesInput() {
		final Panel panel = new FlowPanel();

		final TextBox usercontent = new TextBox();

		// adds a textbox where if you click it, it autoclears the current text
		// in there for you.
		usercontent.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (usercontent.getText() == "Notes") {
					usercontent.setText("");
				}
			}
		});

		final FormPanel form = new FormPanel();
		Panel formElements = new FlowPanel();
		form.setAction("#");
		usercontent.setVisibleLength(60);
		usercontent.addStyleName("textAreaLabels");
		usercontent.setText("Notes");
		Button submit = new Button("Post It");

		submit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});

		formElements.add(submit);
		formElements.add(usercontent);

		form.addSubmitHandler(new SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				String tobeposted = usercontent.getText();
				if (tobeposted.length() <= 0) {
					Window.alert("Your note is blank");
				} else if (tobeposted.length() > 140) {
					Window.alert("Your note is too long");
				} else {
					addNotes(tobeposted, usercontent);
					event.cancel();
				}
			}
		});
		// Build panels for notes:
		form.add(formElements);
		panel.add(form);
		RootPanel.get("notePanel").add(panel);
	}

	private void addNotes(final String content, final TextBox textbox) {
		noteService.addNote(content, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				Window.alert("Note was NOT added");
			}

			@Override
			public void onSuccess(Void result) {
				textbox.setText("");
				notes.clear();
				notesFlexTable.removeAllRows();
				notesFlexTable.setText(0, 0, "Content");
				notesFlexTable.setText(0, 1, "Date");
				notesFlexTable.setText(0, 2, "Tweet It");
				notesFlexTable.setText(0, 3, "Remove");
				notesFlexTable.setCellPadding(6);
				notesPanel.add(notesFlexTable);
				RootPanel.get("noteResult").add(notesPanel);
				loadNotes();
			}
		});
	}

	private void loadNotes() {
		noteService.getNotes(new AsyncCallback<String[]>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(String[] content) {
				if (content.length == 0) {
					userName = loginInfo.getNickname();
					int row = notesFlexTable.getRowCount();
					notesFlexTable.getRowFormatter().addStyleName(0,
							"barossaListHeaderBlack");
					notesFlexTable.getColumnFormatter().addStyleName(0,
							"barossaListHeaderBlack");
					notesFlexTable.getColumnFormatter().addStyleName(1,
							"barossaListHeaderRed");
					notesFlexTable.getColumnFormatter().addStyleName(2,
							"barossaListHeaderYellow");
					notesFlexTable.getColumnFormatter().addStyleName(3,
							"barossaListHeaderRed");
					notesFlexTable.addStyleName("barossaList");
					notesFlexTable.setText(row, 0, userName
							+ ", you have no notes");
					notesFlexTable.getCellFormatter().addStyleName(row, 0,
							"barossaListNumericColumn");
					notesFlexTable.getCellFormatter().addStyleName(row, 1,
							"barossaListNumericColumn");
					notesFlexTable.getCellFormatter().addStyleName(row, 2,
							"barossaListNumericColumn");
					notesFlexTable.getCellFormatter().addStyleName(row, 3,
							"barossaListNumericColumn");
				} else {
					for (int i = content.length - 3; i >= 0; i -= 3) {
						displayNote(content[i], content[i + 1], content[i + 2]);
					}
				}
			}
		});
	}

	private void displayNote(final String content, final String date,
			final String id) {
		int row = notesFlexTable.getRowCount();

		notes.add(id);
		notesFlexTable.getRowFormatter().addStyleName(0,
				"barossaListHeaderBlack");
		notesFlexTable.getColumnFormatter().addStyleName(0,
				"barossaListHeaderBlack");
		notesFlexTable.getColumnFormatter().addStyleName(1,
				"barossaListHeaderRed");
		notesFlexTable.getColumnFormatter().addStyleName(2,
				"barossaListHeaderYellow");
		notesFlexTable.getColumnFormatter().addStyleName(3,
				"barossaListHeaderRed");
		notesFlexTable.addStyleName("barossaList");
		notesFlexTable.setText(row, 0, content);
		notesFlexTable.setText(row, 1, date);
		notesFlexTable.setWidget(row, 2, new Label());
		notesFlexTable.getCellFormatter().addStyleName(row, 0,
				"barossaListNumericColumn");
		notesFlexTable.getCellFormatter().addStyleName(row, 1,
				"barossaListNumericColumn");
		notesFlexTable.getCellFormatter().addStyleName(row, 2,
				"barossaListNumericColumn");
		notesFlexTable.getCellFormatter().addStyleName(row, 3,
				"barossaListNumericColumn");

		// Adds a button to remove this note from the table:
		Button removeNoteButton = new Button("x");
		removeNoteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Window.alert("To be removed");
				long l = Long.parseLong(id);
				removeNote(l);
			}
		});
		notesFlexTable.setWidget(row, 3, removeNoteButton);

		Image tweetImage = new Image("http://i.imgur.com/VLmylMB.png?1");
		tweetImage.setPixelSize(70, 20);
		PushButton twitterButton = new PushButton(tweetImage);

		twitterButton.addClickHandler(new ClickHandler() {
			private int i = 0;

			public void onClick(ClickEvent event) {
				if (twitterInfo.getUser() == null) {
					Window.alert("Not logged in twitter yet");
					return;
				}
				if (i > 0) {
					Window.alert("You already tweeted this note");
					return;
				}
				tweetNote(content);
				i++;
			}
		});
		notesFlexTable.setWidget(row, 2, twitterButton);
	}

	private void removeNote(final long id) {
		noteService.removeNote(id, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
			}

			public void onSuccess(Void ignore) {
				undisplayNote(id);
			}
		});
	}

	private void undisplayNote(long id) {
		String stringId = Long.toString(id);
		int removedIndex = notes.indexOf(stringId);
		notes.remove(removedIndex);
		notesFlexTable.removeRow(removedIndex + 1);
	}

	// ////// END OF NOTES

	// ///// START OF TWITTER LOGIN
	private void logoutTwitter() {
		twitterService.logoutTwitter(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(String logoutUrl) {
				Window.Location.assign(logoutUrl);
			}
		});
	}

	private void twitterLogin() {

		twitterLogoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				logoutTwitter();
			}
		});

		ServiceDefTarget endpoint = (ServiceDefTarget) twitterService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "twitter";
		endpoint.setServiceEntryPoint(moduleRelativeURL);

		twitterService.login(new AsyncCallback<TwitterInfo>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(TwitterInfo cred) {
				if (cred == null) {
					twitterLoginLink
							.setText("An exception occurred on the server");
				} else if (cred.getUser() != null) {
					twitterLabelResult
							.setText("You are currently logged into Twitter as "
									+ cred.getUser());
					twitterLoginLink.setVisible(false);
					twitterInfo.setUser(cred.getUser());
					twitterLogoutButton.setVisible(true);
				} else {
					twitterLoginLink.setHref(cred.getLoginURL());
					twitterLoginLink.setVisible(true);
					twitterLabelResult.setText("Please login with Twitter");
				}
			}
		});
	}

	private void tweetNote(final String symbol) {
		if (twitterInfo.getUser() == null) {
			Window.alert("Not logged in twitter yet");
			return;
		}
		twitterService.tweetNote(symbol, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void ignore) {
				Window.alert("Successfully tweeted note");
			}
		});
	}

	// //// END OF TWITTER SECTION

	private void getListOfLocations() {
		locationService.getItAll(new AsyncCallback<List<Locations>>() {
			public void onFailure(Throwable caught) {
				System.out.println("Error in getAllLiquors");
			}

			public void onSuccess(List<Locations> result) {
				for (Locations a : result) {
					listOfLocations.add(a);
				}
			}
		});
	}

	private void initLiquorsDataset() {
		parseliquorsbutton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				liquorService
						.runLiquorParser(new AsyncCallback<List<Liquors>>() {
							public void onFailure(Throwable caught) {
								Window.alert("Error in runLiquorParser");
							}

							public void onSuccess(List<Liquors> result) {
								Window.alert("Success in loading Liquor Dataset");
							}
						});
				liquorService.getItAll(new AsyncCallback<List<Liquors>>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error in getItAlll liquors");
					}

					public void onSuccess(List<Liquors> result) {
						int counter = result.size();
						Window.alert("There are: " + counter + "Liquors");

					}
				});
			}
		});
	}

	private void initLocationsDataset() {
		parselocationsbutton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				locationService
						.runLocationParser(new AsyncCallback<List<Locations>>() {
							public void onFailure(Throwable caught) {
								Window.alert("Error in runLocationParser");
							}

							public void onSuccess(List<Locations> result) {
								Window.alert("Success in runLocationParser");
							}
						});
				locationService.getItAll(new AsyncCallback<List<Locations>>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error in getItAll Locations ");
					}

					public void onSuccess(List<Locations> result) {
						Window.alert("There are: " + result.size()
								+ "Locations");
					}
				});
			}
		});
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}

	@Override
	public void onFailure() {
	}

	@Override
	public void onSuccess(LatLng point) {
	}

	// Making things look nice:
	private void loadBanner() {
		flagPanel.add(flagBanner);
		RootPanel.get("flagBanner").add(flagPanel);
	}

	private void addDivider(String title, int place) {
		Image dividerImage = new Image("http://i.imgur.com/iEGi3zs.png?4");
		VerticalPanel dividerPanel = new VerticalPanel();
		dividerPanel.add(dividerImage);
		Label text = new Label(title);
		text.setStyleName("dividerText");
		dividerPanel.add(text);
		RootPanel.get("dividerBar" + place).add(dividerPanel);
	}
}
