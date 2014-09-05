package com.wai.seifan.quest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ning.http.client.Response;
import com.wai.seifan.common.Questable;
import com.wai.seifan.dto.QuestInfo;
import com.wai.seifan.util.Utils;

public class QuestTower2 extends Questable {
	private boolean isUsedMana = false;
	private boolean isUsedManaSmall = false;
	private boolean isUsedManaFullLocked = false;
	private boolean isUsedManaFullOpened = false;
	
//	public static void main(String[] args) throws Exception {
//		QuestTower2 tower = new QuestTower2();
//		if (tower.login("hit", "hatrung")) {
//			tower.execute();
//		}
//	}

	@Override
	public void execute() throws Exception {
		while (true) {
			
			// Diem tiem nang // this.getResponse("");
			Long potentialPoint = this.getPotentialPoint();
			logger.info("Hien tai dang co " + potentialPoint + "diem tiem nang.");
			
			Response towerShowInfoResponse = this.getResponse("http://chada.seifan.shopgautho.com/event/chada3_tower_quest/execute/not");
			String 
			towerShowInfoLastURI = towerShowInfoResponse.getUri().toString();
			// kiem tra truong hop roi the
			if (StringUtils.contains(towerShowInfoLastURI, "drop")) {
				// 1. goi den link nhan the
				Response r = this.getResponse("http://chada.seifan.shopgautho.com/event/chada3_tower_quest/execute/get");
				this.getResponse(StringUtils.replace(r.getUri().toString(), "swf_touch","swf"));
				logger.info("VUA NHAN DUOC THE ROI RA !!!");
				continue;
			}
			QuestInfo towerQuest = Utils.getQuestInfo(towerShowInfoResponse);
			if (towerQuest.getCompletePercent() == 100L) {
				// 1. goi den url de qua tang = cach  goi den index
				this.getResponse("http://chada.seifan.shopgautho.com/event/chada3_tower_quest/index");
//				Utils.println(Jsoup.parse(r.getResponseBody()));
				// 2. check neu tang chia het cho 10 thi danh boss
				if (towerQuest.getNo() % 10 == 0) {
					// 1. vao trang danh boss = boss http://chada.seifan.shopgautho.com/event/chada3_tower_quest/boss
					Response bossResponse = this.getResponse("http://chada.seifan.shopgautho.com/event/chada3_tower_quest/boss");
					Document bossDocument = Jsoup.parse(bossResponse.getResponseBody());
//					Utils.println(bossDocument);
					if (bossDocument.select("div.gradiationGray > span > a").first() == null) {
						// bang cach nao do da danh boss xong, chuyen sang lam quest tiep theo
						continue;
					}
					String uriBoss = StringUtils.split(bossDocument.select("div.gradiationGray > span > a").first().attr("href"), "?")[0];
					String idBoss = StringUtils.split(uriBoss, "/")[3];
					// 2. tim url de danh boss va goi den viec danh boss http://chada.seifan.shopgautho.com/swf_touch/201404202060/boss_battle_result/20016
					this.getResponse("http://chada.seifan.shopgautho.com/swf_touch/201404202060/boss_battle_result/"+idBoss);
					// 3. goi den trang ket qua de ket thuc danh boss http://chada.seifan.shopgautho.com/event/chada3_tower_quest/result/20016
					this.getResponse("http://chada.seifan.shopgautho.com/event/chada3_tower_quest/result/"+idBoss);
					logger.info("BAN VUA DANH BOSS XONG");
					// 4. Check neu danh boss tang 30 xong thi reset ve tang 1 de tiet kiem tinh luc, chua lam ngay bay gio
//					if (towerQuest.getNo() == 30L) {
//						Utils.doResetTowerQuest(client, session);	
//					}
				}
			} else if (towerQuest.getManaHad() >= towerQuest.getManaCost()) {
				// 1. thuc hien leo thap binh thuong
				this.doQuest(null);
			} else {
				// khong du mana leo thap
				if (towerQuest.getManaHad() + potentialPoint >= towerQuest.getManaCost()) {
					// 1. cong diem tiem nang du de leo thap
					this.doAddUserParam(towerQuest.getManaCost() - towerQuest.getManaHad(), null, null);
					// 2. thuc hien leo thap binh thuong
					this.doQuest(null);
				} else {
					// tinh exp can thiet de len level
					Long expNeedToLevelUp = towerQuest.getExpTotal() - towerQuest.getExpCurrent();
					
					if (!isUsedMana) {
						logger.info("KHONG SU DUNG NGOC");
						Long waitTime = (towerQuest.getManaCost() - towerQuest.getManaHad())*2;
						logger.info("HET NGOC : DOI " + waitTime + " PHUT DE HOI PHUC MANA !!!");
						Thread.sleep(waitTime*60*1000);
					}
					if (isUsedManaFullLocked) {
						// tinh toan viec an tinh luc full truoc
						Long willManaHaveFull = towerQuest.getManaTotal();
						Long willExpHaveFull = (willManaHaveFull/towerQuest.getManaCost())*towerQuest.getExpGain();
						if (willExpHaveFull <= expNeedToLevelUp) {
							if (potentialPoint > 0) this.doAddUserParam(potentialPoint, null, null);

							// 2. Neu con tinh luc full						
							if (hasManaBottleFull()) {
								//   1.1 An tinh luc full --> thuc hien luon luc kiem tra
								//   1.2 Leo thap binh thuong
								this.doQuest(null);
								//   1.3 Thoat khoi viec tinh toan voi tinh luc small
								continue;
							} 					
							// 2. Neu het tinh luc full, di den buoc tiep theo voi tinh luc small
							
						}
						
					}
					
					if (isUsedManaSmall) {
						// tinh toan viec an tinh luc small
						Long willManaHaveSmall = towerQuest.getManaHad() + 50L;
						Long willExpHaveSmall = (willManaHaveSmall/towerQuest.getManaCost())*towerQuest.getExpGain();
						if (willExpHaveSmall <= expNeedToLevelUp) {
							if (hasManaBottleSmall()) { // 1. Neu con tinh luc small
								//   1.1 An tinh luc small --> thuc hien luon luc kiem tra
								//   1.2 Leo thap binh thuong
								this.doQuest(null);
							} else { // 2. Neu het tinh luc small
								//   2.1 Hien thi thong bao la het ngoc tinh luc
//								Utils.println("HET TAT CA MOI THU DE PHUC HOI MANA !!!!!!");
								//   2.2 Doi bao nhieu phut de hoi phuc mana va chay tiep
								Long waitTime = (towerQuest.getManaCost() - towerQuest.getManaHad())*2;
								logger.info("HET NGOC : DOI " + waitTime + " PHUT DE HOI PHUC MANA !!!");
//								Thread.sleep(waitTime*60*1000);
							}
						} else {
							// an linh luc thua diem de len level
//							Utils.println("NEU AN TINH LUC SE THUA DIEM DE LEN LEVEL");
							Long waitTime = (towerQuest.getManaCost() - towerQuest.getManaHad())*2;
							logger.info("AN TINH LUC THI THUA EXP : DOI " + waitTime + " PHUT DE HOI PHUC MANA !!!");
//							Thread.sleep(waitTime*60*1000);
						}
					}
					
				}
			}
		}
		
	}
	
	public void doAddUserParam(Long mana, Long attack, Long defense) throws Exception {
		// 1. cong diem linh luc
		Response userParamReponse = this.getResponse("http://chada.seifan.shopgautho.com/user/param");
		Document doc = Jsoup.parse(userParamReponse.getResponseBody());
		// tim den up linh luc link
		String upLink = "http://chada.seifan.shopgautho.com/" + doc.getElementById("energy_max").parent().attr("action");
		this.getResponse(upLink, "data%5Benergy_max%5D="+ mana);
		// 2. cong diem tinh luc tan cong
		// 3. cong diem tinh luc phong thu
	}
	
	public boolean hasManaBottleFull() throws Exception {
		if (!hasManaBottleFull("8")) {
			if (this.isUsedManaFullOpened) {
				return hasManaBottleFull( "1");
			}
			return false;
		}
		return true;
	}
	
	public boolean hasManaBottleFull(String itemId) throws Exception {
		Response userParamReponse = this.getResponse("http://chada.seifan.shopgautho.com/item/view/"+itemId);
		Document document = Jsoup.parse(userParamReponse.getResponseBody());
		// tim den up linh luc link
		if (document.select("form").first() == null) {
			return false;
		}
		String useLink = "http://chada.seifan.shopgautho.com/" + document.select("form").first().attr("action");
		this.getResponse(useLink, "data%5Bitem_id%5D="+itemId+"&data%5Buse_quantity%5D=1");
		System.out.println("BAN VUA AN TINH LUC FULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL !!!");
		return true;
	}
	
	public boolean hasManaBottleSmall() throws Exception {
		Response response = this.getResponse("http://chada.seifan.shopgautho.com/item/view/100");
		Document document = Jsoup.parse(response.getResponseBody());
		// tim den up linh luc link
		if (document.select("form").first() == null) {
			return false;
		}
		String useLink = "http://chada.seifan.shopgautho.com/" + document.select("form").first().attr("action");
		response = this.getResponse(useLink, "data%5Bitem_id%5D=100&data%5Buse_quantity%5D=1");
		System.out.println("BAN VUA AN TINH LUC NHOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO !!!");
		return true;
	}
	
	@Override
	public void doQuest(String level) throws Exception {
		this.getResponse("http://chada.seifan.shopgautho.com/event/chada3_tower_quest/execute");
	}

	@Override
	public QuestInfo getQuestInfo(Response response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
