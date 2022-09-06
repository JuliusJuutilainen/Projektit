using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class WeaponScript : MonoBehaviour
{
    //Weapon projectile
    public GameObject projectile;

    //Projectile force
    public float gunForce, upwardForce;

    //Stats
    public float interval, spread, reloadT, shotInterval;
    public int magSize, bulletsPerTap;
    public bool allowButtonHold;

    int ammoLeft, ammoShot;

    bool shooting, readyToShoot, reloading;

    //REfecrence
    public Camera playerCam;
    public Transform attackPoint;

    public bool allowInvoke = true;

    //Graphics
    public GameObject muzzleFlash;
    public TextMeshProUGUI ammunitionDisplay;

    //public Rigidbody playerRb;
    public float recoilForce;
    private void Awake()
    {
        //Mag full
        ammoLeft= magSize;
        readyToShoot = true;
    }

    private void Update()
    {
        MyInput();

        if(ammunitionDisplay != null){
            ammunitionDisplay.SetText(ammoLeft/bulletsPerTap + " / "+magSize/bulletsPerTap);
        }
        else{
            ammunitionDisplay.SetText("");
        }
    }
    private void MyInput()
    {
        //Check if allowed to hold down button
        if (allowButtonHold) shooting = Input.GetKey(KeyCode.Mouse0);
        else shooting = Input.GetKeyDown(KeyCode.Mouse0);

        //REloading
        if (Input.GetKeyDown(KeyCode.R) && ammoLeft < magSize){
            Reload();

        }

        if(readyToShoot && shooting && !reloading && ammoLeft<=0) Reload();

        //shooting
        if (readyToShoot && shooting && !reloading && ammoLeft >0)
        {
                ammoShot = 0;

                Shoot();
        }

        
    }


    private void Shoot()
    {
        readyToShoot = false;

        //Find the exact hit position using raycast
        Ray ray = playerCam.ViewportPointToRay(new Vector3(0.5f,0.5f,0));
        RaycastHit hit;

        //check if ray hits something
        Vector3 targetPoint;
        if(Physics.Raycast(ray, out hit)){
            targetPoint = hit.point;
        }

        else{
            targetPoint = ray.GetPoint(75); //Its just far away
        }

        //Calculater direction from attack point to target
        Vector3 directionWithoutSpread = targetPoint- attackPoint.position;

        //Spread calculating, use 0 for axe
        float x = Random.Range(-spread, spread);
        float y = Random.Range(-spread, spread);

        //New direction with spread
        Vector3 directionWithSpread = directionWithoutSpread + new Vector3(x,y,0);

        //We dont use projectiles because fuck you
        GameObject currentBullet = Instantiate(projectile, attackPoint.position, Quaternion.identity);
        currentBullet.transform.forward = directionWithSpread;

        currentBullet.GetComponent<Rigidbody>().AddForce(directionWithSpread.normalized*gunForce, ForceMode.Impulse);

        //Recoil to the player
        

        ammoLeft--;
        ammoShot++;

        //Invoke resetShot
        if (allowInvoke){
            Invoke("ResetShot", interval);
            allowInvoke = false;

            //Recoil to player
            //playerRb.AddForce(-directionWithSpread.normalized * recoilForce, ForceMode.Impulse);
        }

        if(ammoShot < bulletsPerTap && ammoLeft >0){
            Invoke("Shoot", shotInterval);
        }

        //Muzzle flash if you have one
        if (muzzleFlash != null){
            Instantiate(muzzleFlash, attackPoint.position, Quaternion.identity);
        }
    }

    private void ResetShot(){
        readyToShoot = true;
        allowInvoke = true;
    }

    private void Reload(){
        reloading = true;
        Invoke("ReloadFinished", reloadT);


    }

    private void ReloadFinished(){
        ammoLeft = magSize;
        reloading = false;
    }

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    
}
